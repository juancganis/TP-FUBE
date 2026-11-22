package com.tiendaonline.clientes;

import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiendaonline.clientes.controller.ClienteController;
import com.tiendaonline.clientes.dto.ClienteDto;
import com.tiendaonline.clientes.exception.ClienteExistenteException;
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
    public void registrarClienteExitoso() throws JsonProcessingException, Exception {
        ClienteDto clienteDto = new ClienteDto("Juan Perez", "juanperez@gmail.com");
        when(service.registrarCliente(clienteDto)).thenReturn(clienteDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(clienteDto))
        )
        .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test 
    public void registrarClienteYaExistente() throws JsonProcessingException, Exception {
        ClienteDto clienteDto = new ClienteDto("Juan Perez", "juanperez@gmail.com");
        when(service.registrarCliente(clienteDto))
            .thenThrow(new ClienteExistenteException("Ya existe un cliente con ese email"));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(clienteDto))
        )
        .andExpect(MockMvcResultMatchers.status().isConflict());
    }


    /*@Test
    public void consultarClientePorId() {

    }

    @Test 
    public void consultarPedidosCliente() {

    }*/
}
