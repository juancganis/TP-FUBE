package com.tiendaonline.clientes.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiendaonline.clientes.dto.ClienteDto;
import com.tiendaonline.clientes.models.Cliente;
import com.tiendaonline.clientes.repository.ClienteRepository;

@SpringBootTest 
@AutoConfigureMockMvc
public class ClienteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void vaciarBDD(){
        // Limpiamos el repositorio antes de cada prueba
        clienteRepository.deleteAll();
    }

    @Test 
    public void crearClienteFlujoCompleto() throws Exception {
        //Given: cargamos los datos del cliente a crear
        ClienteDto clienteDto = new ClienteDto("Juan Perez", "juanperez@gmail.com");

        //When: realizamos la consulta al endpoint
        mockMvc.perform(post("/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre").value("Juan Perez"))
            .andExpect(jsonPath("$.email").value("juanperez@gmail.com"));

        //Then: Verificamos que realmente se persistió
        assertEquals(1, clienteRepository.count());
        Cliente guardado = clienteRepository.findAll().get(0);
        assertEquals("Juan Perez", guardado.getNombre());
        assertEquals("juanperez@gmail.com", guardado.getEmail());
    }

    @Test
    public void crearCliente_CuandoClienteYaExiste_DevuelveConflict() throws Exception {
        //Given: cargamos los datos del cliente a crear
        ClienteDto clienteDto = new ClienteDto("Juan Perez", "juanperez@gmail.com");
        
        //Creamos el body una vez para no repetir codigo
        String body = objectMapper.writeValueAsString(clienteDto);

        //When: cargamos el cliente por primera vez
        mockMvc.perform(post("/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated()); //201

        //Volvemos a realizar la consulta con los mismos datos de cliente
        mockMvc.perform(post("/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isConflict()); //409

        //Then: nos aseguramos que solo se creo un cliente
        assertEquals(1, clienteRepository.count());
    }
}
