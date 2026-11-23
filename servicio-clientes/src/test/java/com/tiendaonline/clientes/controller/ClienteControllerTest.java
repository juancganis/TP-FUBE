package com.tiendaonline.clientes.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiendaonline.clientes.dto.ClienteDto;
import com.tiendaonline.clientes.dto.ItemPedidoDto;
import com.tiendaonline.clientes.dto.PedidoDto;
import com.tiendaonline.clientes.enums.EstadoPedido;
import com.tiendaonline.clientes.exception.ClienteExistenteException;
import com.tiendaonline.clientes.exception.ClienteNoExistentePorIdException;
import com.tiendaonline.clientes.service.ClienteService;

@WebMvcTest (ClienteController.class)
public class ClienteControllerTest {

    @Autowired 
    private MockMvc mockMvc;

    @Autowired 
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService service;

    @Test
    public void registrarClienteExitoso() throws Exception {
        ClienteDto clienteDto = new ClienteDto("Juan Perez", "juanperez@gmail.com");
        when(service.registrarCliente(clienteDto)).thenReturn(clienteDto);

        mockMvc.perform(post("/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre").value("Juan Perez"))
            .andExpect(jsonPath("$.email").value("juanperez@gmail.com"));
    }

    @Test
    public void registrarClienteYaExistente() throws Exception {
        ClienteDto clienteDto = new ClienteDto("Juan Perez", "juanperez@gmail.com");
        when(service.registrarCliente(clienteDto))
            .thenThrow(new ClienteExistenteException("Ya existe un cliente con ese email"));

        mockMvc.perform(post("/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDto)))
            .andExpect(status().isConflict());
    }

    @Test
    public void registrarClienteConEmailInvalido() throws Exception {
        ClienteDto clienteInvalido = new ClienteDto("Juan Perez", "email-invalido");

        mockMvc.perform(post("/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInvalido)))
            .andExpect(status().isBadRequest());

        // Si la validación falla, el service ni siquiera debe ser invocado
        verify(service, never()).registrarCliente(any());
    }

    @Test
    public void consultarClientePorId() throws Exception {
        Integer id = 1;
        when(service.consultarClientePorId(id))
            .thenReturn(new ClienteDto("Juan Perez", "juanperez@gmail.com"));

        mockMvc.perform(get("/v1/clientes/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Juan Perez"))
            .andExpect(jsonPath("$.email").value("juanperez@gmail.com"));
    }

    @Test
    public void consultarClientePorIdNoExistente() throws Exception {
        Integer id = 1;
        when(service.consultarClientePorId(id))
            .thenThrow(new ClienteNoExistentePorIdException("El cliente no existe"));

        mockMvc.perform(get("/v1/clientes/{id}", id))
            .andExpect(status().isNotFound());
    }

    @Test
    public void consultarPedidosCliente() throws Exception {
        LocalDateTime fecha = LocalDateTime.of(2026, 11, 20, 10, 30, 0);
        Integer clienteId = 1;

        ItemPedidoDto itemPedidoDto = ItemPedidoDto.builder()
            .itemPedidoId(5)
            .productoId(15)
            .cantidad(3)
            .precioUnitario(new BigDecimal("5000.00"))
            .build();
        
        PedidoDto pedidoDto = PedidoDto.builder()
            .pedidoId(10)
            .clienteId(clienteId)
            .estado(EstadoPedido.CREADO)
            .montoTotal(new BigDecimal("15000.00"))
            .fechaCreacion(fecha)
            .fechaActualizacion(fecha)
            .listaItemsPedido(List.of(itemPedidoDto))
            .build();

        when(service.consultarPedidosCliente(clienteId)).thenReturn(List.of(pedidoDto));

        mockMvc.perform(get("/v1/clientes/{id}/pedidos", clienteId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].pedidoId").value(10))
            .andExpect(jsonPath("$[0].clienteId").value(clienteId))
            .andExpect(jsonPath("$[0].estado").value("CREADO"))
            .andExpect(jsonPath("$[0].montoTotal").value(15000.0))
            .andExpect(jsonPath("$[0].fechaCreacion").value("2026-11-20T10:30:00"))
            .andExpect(jsonPath("$[0].fechaActualizacion").value("2026-11-20T10:30:00"))
            .andExpect(jsonPath("$[0].listaItemsPedido.length()").value(1))
            .andExpect(jsonPath("$[0].listaItemsPedido[0].itemPedidoId").value(5))
            .andExpect(jsonPath("$[0].listaItemsPedido[0].productoId").value(15))
            .andExpect(jsonPath("$[0].listaItemsPedido[0].cantidad").value(3))
            .andExpect(jsonPath("$[0].listaItemsPedido[0].precioUnitario").value(5000.0));
    }
}
