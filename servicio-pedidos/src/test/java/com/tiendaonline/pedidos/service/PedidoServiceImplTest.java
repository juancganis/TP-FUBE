package com.tiendaonline.pedidos.service;

import java.util.List;
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

    @Test
    void cancelarPedido_CuandoEstadoEsCreado_DebeCancelarYLiberarStock() {

        // Given: un pedido existente en estado CREADO.
        Integer pedidoId = 1;
        Pedido pedidoExistente = Pedido.builder()
                .id(pedidoId)
                .clienteId(10)
                .estado(EstadoPedido.CREADO)
                .montoTotal(new BigDecimal("1500.00"))
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
        Integer pedidoId = 2;
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

}
