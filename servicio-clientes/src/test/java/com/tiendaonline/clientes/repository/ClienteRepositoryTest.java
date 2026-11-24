package com.tiendaonline.clientes.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.tiendaonline.clientes.models.Cliente;

@DataJpaTest(properties = {
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void existsByEmail_CuandoElEmailExiste_DevuelveTrue() {
        Cliente cliente = new Cliente("Juan Perez", "juanperez@gmail.com",
                LocalDateTime.of(2026, 11, 20, 10, 30, 0));
        clienteRepository.save(cliente);

        boolean existe = clienteRepository.existsByEmail("juanperez@gmail.com");

        assertTrue(existe);
    }

    @Test
    void existsByEmail_CuandoElEmailNoExiste_DevuelveFalse() {
        boolean existe = clienteRepository.existsByEmail("inexistente@gmail.com");

        assertFalse(existe);
    }
}