package com.tiendaonline.pedidos.service;

import java.util.List;

import com.tiendaonline.pedidos.dto.ItemPedidoRequestDto;
import com.tiendaonline.pedidos.dto.PedidoRequestDto;
import com.tiendaonline.pedidos.dto.ProductoResponseDto;
import org.mockito.Mock;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import com.tiendaonline.pedidos.models.Pedido;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.tiendaonline.pedidos.enums.EstadoPedido;
import com.tiendaonline.pedidos.client.ClienteClient;
import com.tiendaonline.pedidos.dto.PedidoResponseDto;
import com.tiendaonline.pedidos.client.InventarioClient;
import com.tiendaonline.pedidos.repository.PedidoRepository;
import com.tiendaonline.pedidos.exception.RecursoNoEncontradoException;
import com.tiendaonline.pedidos.exception.EstadoPedidoInvalidoException;

// Con esta anotación habilitamos Mockito en JUnit 5 sin levantar el contexto completo de Spring:
@ExtendWith(MockitoExtension.class)
public class PedidoServiceImplTest {

    // Definimos los mocks de las dependencias externas e internas de PedidoServiceImpl:

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private InventarioClient inventarioClient;

    // Definimos una instancia real del servicio con los mocks inyectados automáticamente:
    @InjectMocks
    private PedidoServiceImpl pedidoService;


    // DE ACÁ EN ADELANTE Y HASTA QUE SE AVISE, TODOS LOS TESTS LOS HICE CON TDD:
    // -------------------------------------------------------------------------

    // TESTS PARA 'cancelarPedido':

    @Test
    void cancelarPedido_CuandoEstadoEsCreado_DebeCancelarYLiberarStock() {

        // Given: un pedido existente en estado CREADO.
        Integer pedidoId = 1;
        Pedido pedidoExistente = Pedido.builder()
                .id(pedidoId)
                .clienteId(1)
                .estado(EstadoPedido.CREADO)
                .montoTotal(new BigDecimal("45000.00"))
                .fechaCreacion(LocalDateTime.now())
                .listaItemsPedido(List.of())
                .build();

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoExistente));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: ejecutamos la cancelación.
        PedidoResponseDto resultado = pedidoService.cancelarPedido(pedidoId);

        // Then: verificamos estado CANCELADO, persistencia y llamada a inventario para liberar stock.
        assertNotNull(resultado);
        assertEquals(EstadoPedido.CANCELADO, resultado.estado());
        verify(inventarioClient, times(1)).liberarReservasPorPedido(pedidoId);
        verify(pedidoRepository, times(1)).save(pedidoExistente);
    }

    @Test
    void cancelarPedido_CuandoPedidoNoExiste_DebeLanzarRecursoNoEncontradoException() {

        // Given: un ID inexistente en base de datos.
        Integer pedidoIdInexistente = 999;
        when(pedidoRepository.findById(pedidoIdInexistente)).thenReturn(Optional.empty());

        // When & Then: debe lanzar RecursoNoEncontradoException y no interactuar con inventario.
        assertThrows(RecursoNoEncontradoException.class, () -> pedidoService.cancelarPedido(pedidoIdInexistente));
        verifyNoInteractions(inventarioClient);
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void cancelarPedido_CuandoYaEstaCanceladoOExpirado_DebeLanzarEstadoPedidoInvalidoException() {

        // Given: un pedido que ya fue cancelado previamente.
        Integer pedidoId = 19;
        Pedido pedidoYaCancelado = Pedido.builder()
                .id(pedidoId)
                .estado(EstadoPedido.CANCELADO)
                .build();

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoYaCancelado));

        // When & Then: rechaza la operación y no vuelve a liberar stock.
        assertThrows(EstadoPedidoInvalidoException.class, () -> pedidoService.cancelarPedido(pedidoId));
        verifyNoInteractions(inventarioClient);
        verify(pedidoRepository, never()).save(any());
    }

    // A PARTIR DE ACÁ, LOS TESTS NO LOS HICE CON TDD:
    // -----------------------------------------------

    // TESTS PARA 'crearPedido':

    @Test
    void crearPedido_CuandoDatosSonValidos_DebeCalcularMontoReservarStockYGuardar() {

        // Given: un request válido con 2 unidades de un producto de $12000.00.
        var itemRequest = ItemPedidoRequestDto.builder()
                .sku("REM-NE-XS")
                .cantidad(2)
                .build();

        var requestDto = PedidoRequestDto.builder()
                .clienteId(10)
                .listaItemsPedido(List.of(itemRequest))
                .build();

        var productoMock = ProductoResponseDto.builder()
                .id(1)
                .sku("REM-NE-XSE")
                .precioUnitario(new BigDecimal("12000.00"))
                .build();

        when(inventarioClient.obtenerProducto("REM-NE-XS")).thenReturn(productoMock);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedidoGuardado = invocation.getArgument(0);
            pedidoGuardado.setId(100);
            return pedidoGuardado;
        });

        // When: ejecutamos la creación del pedido.
        PedidoResponseDto resultado = pedidoService.crearPedido(requestDto);

        // Then: verificamos estado CREADO, monto total ($3000.00) e interacciones con clientes/inventario.
        assertNotNull(resultado);
        assertEquals(100, resultado.pedidoId());
        assertEquals(EstadoPedido.CREADO, resultado.estado());
        assertEquals(new BigDecimal("24000.00"), resultado.montoTotal());
        verify(clienteClient, times(1)).validarExistenciaCliente(10);
        verify(inventarioClient, times(1)).reservarStock(any());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void crearPedido_CuandoClienteNoExiste_DebeLanzarRecursoNoEncontradoException() {

        // Given: clienteClient lanza excepción al validar un cliente inexistente.
        var requestDto = PedidoRequestDto.builder()
                .clienteId(999)
                .listaItemsPedido(List.of(ItemPedidoRequestDto.builder().sku("001-RM-NE").cantidad(1).build()))
                .build();

        doThrow(new RecursoNoEncontradoException("No existe el cliente con ID: 999"))
                .when(clienteClient).validarExistenciaCliente(999);

        // When & Then: debe propagar la excepción sin consultar inventario ni guardar en BD.
        assertThrows(RecursoNoEncontradoException.class, () -> pedidoService.crearPedido(requestDto));
        verifyNoInteractions(inventarioClient);
        verify(pedidoRepository, never()).save(any());
    }

    // TESTS PARA 'obtenerPedidoPorId':

    @Test
    void obtenerPedidoPorId_CuandoExiste_DebeRetornarPedidoResponseDto() {

        // Given: un pedido existente en el repositorio.
        Integer pedidoId = 1;
        Pedido pedidoExistente = Pedido.builder()
                .id(pedidoId)
                .clienteId(1)
                .estado(EstadoPedido.CREADO)
                .montoTotal(new BigDecimal("45000.00"))
                .fechaCreacion(LocalDateTime.now())
                .listaItemsPedido(List.of())
                .build();

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoExistente));

        // When: buscamos el pedido por ID.
        PedidoResponseDto resultado = pedidoService.obtenerPedidoPorId(pedidoId);

        // Then: devuelve el DTO mapeado correctamente.
        assertNotNull(resultado);
        assertEquals(pedidoId, resultado.pedidoId());
        assertEquals(1, resultado.clienteId());
        assertEquals(new BigDecimal("45000.00"), resultado.montoTotal());
    }

    @Test
    void obtenerPedidoPorId_CuandoNoExiste_DebeLanzarRecursoNoEncontradoException() {

        // Given: un ID inexistente.
        when(pedidoRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then: debe lanzar RecursoNoEncontradoException.
        assertThrows(RecursoNoEncontradoException.class, () -> pedidoService.obtenerPedidoPorId(999));
    }

    // TESTS PARA 'listarPedidosPorCliente':

    @Test
    void listarPedidosPorCliente_CuandoExistenPedidos_DebeRetornarListaDeDtos() {

        // Given: dos pedidos asociados al cliente 10.
        Integer clienteId = 10;
        Pedido p1 = Pedido.builder().id(1).clienteId(clienteId).estado(EstadoPedido.CREADO)
                .montoTotal(new BigDecimal("1000.00")).listaItemsPedido(List.of()).build();
        Pedido p2 = Pedido.builder().id(2).clienteId(clienteId).estado(EstadoPedido.CANCELADO)
                .montoTotal(new BigDecimal("2000.00")).listaItemsPedido(List.of()).build();

        when(pedidoRepository.findByClienteId(clienteId)).thenReturn(List.of(p1, p2));

        // When: listamos los pedidos del cliente.
        List<PedidoResponseDto> resultado = pedidoService.listarPedidosPorCliente(clienteId);

        // Then: retorna ambos pedidos mapeados a DTO.
        assertEquals(2, resultado.size());
        verify(pedidoRepository, times(1)).findByClienteId(clienteId);
    }

}