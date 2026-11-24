package com.tiendaonline.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tiendaonline.clientes.client.PedidosClient;
import com.tiendaonline.clientes.dto.ClienteDto;
import com.tiendaonline.clientes.dto.PedidoDto;
import com.tiendaonline.clientes.enums.EstadoPedido;
import com.tiendaonline.clientes.exception.ClienteExistenteException;
import com.tiendaonline.clientes.exception.ClienteNoExistentePorIdException;
import com.tiendaonline.clientes.exception.ServicioExternoException;
import com.tiendaonline.clientes.models.Cliente;
import com.tiendaonline.clientes.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceImplTest {
    @Mock 
    private ClienteRepository clienteRepository;

    @Mock 
    private PedidosClient pedidosClient;

    @Mock 
    private FeatureFlagService featureFlagService;

    @InjectMocks 
    private ClienteServiceImpl clienteService;

    @Test 
    void consultarClientePorId_CuandoMaskPiiEsTrue_DebeEnmascararElCliente() {
        //Given: Cliente existente con id 1
        Integer clienteId = 1;
        LocalDateTime fecha = LocalDateTime.of(2026, 11, 20, 10, 30, 0);
        Cliente cliente = new Cliente("Juan Perez", "juanperez@gmail.com", fecha);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(featureFlagService.isEnabled("mask-pii", String.valueOf(clienteId))).thenReturn(true);

        //When: se ejecuta la consulta
        ClienteDto resultado = clienteService.consultarClientePorId(clienteId);

        //Then: verificamos datos enmascarados y llamadas a repository y servicio de feature flags
        assertEquals(cliente.nombreEnmascarado(), resultado.nombre());
        assertEquals(cliente.emailEnmascarado(), resultado.email());
        verify(clienteRepository).findById(clienteId);
        verify(featureFlagService).isEnabled("mask-pii", String.valueOf(clienteId));
    }

    @Test
    void consultarClientePorId_CuandoMaskPiiEsFalse_DebeDevolverDatosReales() {
        //Given: Cliente existente con id 1
        Integer clienteId = 1;
        Cliente cliente = new Cliente("Juan Perez", "juanperez@gmail.com", LocalDateTime.of(2026, 11, 20, 10, 30, 0));

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(featureFlagService.isEnabled("mask-pii", String.valueOf(clienteId))).thenReturn(false);

        //When: se ejecuta la consulta
        ClienteDto resultado = clienteService.consultarClientePorId(clienteId);

        //Then: verificamos datos reales y llamadas a repository y servicio de feature flags
        assertEquals("Juan Perez", resultado.nombre());
        assertEquals("juanperez@gmail.com", resultado.email());
        verify(clienteRepository).findById(clienteId);
        verify(featureFlagService).isEnabled("mask-pii", String.valueOf(clienteId));
    }

    @Test 
    void consultarClientePorId_CuandoClienteNoExiste_DebeLanzarClienteNoExistentePorIdException() {
        //Given: Cliente existente con id 1
        Integer clienteId = 1;
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        //Se verifica que cuando se ejecute la consulta el service arroje una excepcion        
        ClienteNoExistentePorIdException ex = assertThrows(ClienteNoExistentePorIdException.class,
            () -> clienteService.consultarClientePorId(clienteId));
        
        //Then: Se verifica que el mensaje de la excepcion sea el correcto y las interacciones
        assertEquals("El cliente no existe", ex.getMessage());
        verify(clienteRepository).findById(clienteId);
        verifyNoInteractions(featureFlagService);
    }

    @Test 
    void registrarCliente_CuandoClienteNoExiste_DebeGuardarUnNuevoCliente() {
        ClienteDto clienteDto = new ClienteDto("Juan Perez", "juanperez@gmail.com");

        when(clienteRepository.existsByEmail(clienteDto.email())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        ClienteDto respuesta = clienteService.registrarCliente(clienteDto);

        assertEquals(clienteDto, respuesta);

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteRepository).existsByEmail(clienteDto.email());
        verify(clienteRepository).save(captor.capture());

        Cliente guardado = captor.getValue();
        assertEquals("Juan Perez", guardado.getNombre());
        assertEquals("juanperez@gmail.com", guardado.getEmail());
        assertNotNull(guardado.getFechaAlta());
    }

    @Test 
    void registrarCliente_CuandoClienteExiste_DebeLanzarClienteExistenteExceptionYNoGuardarlo() {
        ClienteDto clienteDto = new ClienteDto("Juan Perez", "juanperez@gmail.com");

        when(clienteRepository.existsByEmail(clienteDto.email())).thenReturn(true);

        ClienteExistenteException ex = assertThrows(ClienteExistenteException.class, 
                () -> clienteService.registrarCliente(clienteDto));

        assertEquals("Ya existe un cliente con ese email", ex.getMessage());

        verify(clienteRepository).existsByEmail(clienteDto.email());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void consultarPedidosCliente_CuandoClienteExiste_DevuelvePedidosDelClient() {
        Integer clienteId = 1;
        LocalDateTime fecha = LocalDateTime.of(2026, 11, 20, 10, 30, 0);
        Cliente cliente = new Cliente("Juan Perez", "juanperez@gmail.com", fecha);

        PedidoDto pedidoDto = PedidoDto.builder()
            .pedidoId(10)
            .clienteId(clienteId)
            .estado(EstadoPedido.CREADO)
            .montoTotal(new BigDecimal("15000.00"))
            .fechaCreacion(fecha)
            .fechaActualizacion(fecha)
            .listaItemsPedido(List.of())
            .build();
        List<PedidoDto> pedidos = List.of(pedidoDto);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(pedidosClient.consultarPedidosCliente(clienteId)).thenReturn(pedidos);

        List<PedidoDto> resultado = clienteService.consultarPedidosCliente(clienteId);

        assertEquals(pedidos, resultado);
        verify(clienteRepository).findById(clienteId);
        verify(pedidosClient).consultarPedidosCliente(clienteId);
    }

    @Test
    void consultarPedidosCliente_CuandoClienteNoExiste_LanzaClienteNoExistentePorIdYNoLlamaAPedidosClient() {
        Integer clienteId = 1;
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        ClienteNoExistentePorIdException ex = assertThrows(ClienteNoExistentePorIdException.class,
            () -> clienteService.consultarPedidosCliente(clienteId));

        assertEquals("El cliente no existe", ex.getMessage());
        verify(clienteRepository).findById(clienteId);
        verifyNoInteractions(pedidosClient);
    }

    @Test
    void consultarPedidosCliente_CuandoServicioPedidosCaido_PropagaServicioExternoException() {
        Integer clienteId = 1;
        Cliente cliente = new Cliente("Juan Perez", "juanperez@gmail.com", LocalDateTime.of(2026, 11, 20, 10, 30, 0));

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(pedidosClient.consultarPedidosCliente(clienteId))
            .thenThrow(new ServicioExternoException("El servicio de pedidos no está disponible en este momento"));

        ServicioExternoException ex = assertThrows(ServicioExternoException.class,
            () -> clienteService.consultarPedidosCliente(clienteId));

        assertEquals("El servicio de pedidos no está disponible en este momento", ex.getMessage());
        verify(pedidosClient).consultarPedidosCliente(clienteId);
    }
}
