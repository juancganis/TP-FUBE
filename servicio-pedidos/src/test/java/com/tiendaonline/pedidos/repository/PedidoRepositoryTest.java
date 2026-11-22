package com.tiendaonline.pedidos.repository;

import com.tiendaonline.pedidos.enums.EstadoPedido;
import com.tiendaonline.pedidos.models.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Con esta anotacion levantamos únicamente los componentes JPA (entidades, repositorios y EntityManager) conectados a una base de datos en memoria H2.
// Ademas, cada @Test corre dentro de una transacción que hace rollback automático al finalizar.
@DataJpaTest

// Sobrescribimos propiedades de PostgreSQL/Flyway para que H2 cree el esquema desde las entidades Java (sin ejecutar scripts de PostgresSQL):
@TestPropertySource(properties = {"spring.flyway.enabled=false", "spring.jpa.hibernate.ddl-auto=create-drop"})
public class PedidoRepositoryTest {

    // Inyectamos el repositorio real (sin mocks) porque su dependencia es la base de datos en memoria H2.
    @Autowired
    private PedidoRepository pedidoRepository;
    private LocalDateTime ahora;

    // @BeforeEach se ejecuta antes de cada @Test.
    // En este caso es para crear y persistir 3 clientes de prueba (con diferentes caracteristicas y casos) para poder usarlos "nuevos" antes de probar cada test.
    @BeforeEach
    void setUp() {

        ahora = LocalDateTime.now();

        // Pedido 1: cliente con id 10, estado CREADO, hace 30 horas (vencido para el proceso batch).
        Pedido pedidoVencidoCliente10 = Pedido.builder()
                .clienteId(10)
                .estado(EstadoPedido.CREADO)
                .montoTotal(new BigDecimal("1000.00"))
                .fechaCreacion(ahora.minusHours(30))
                .build();

        // Pedido 2: cliente con id 10, estado CONFIRMADO, hace 2 horas (vigente, no debería expirar).
        Pedido pedidoConfirmadoCliente10 = Pedido.builder()
                .clienteId(10)
                .estado(EstadoPedido.CONFIRMADO)
                .montoTotal(new BigDecimal("2500.00"))
                .fechaCreacion(ahora.minusHours(2))
                .build();

        // Pedido 3: cliente con id 20, estado CREADO, hace 40 horas (vencido, pero de otro cliente).
        Pedido pedidoVencidoCliente20 = Pedido.builder()
                .clienteId(20)
                .estado(EstadoPedido.CREADO)
                .montoTotal(new BigDecimal("500.00"))
                .fechaCreacion(ahora.minusHours(40))
                .build();

        // Generamos una lista de esos 3 clientes de prueba creados y la persistimos en la base de datos en memoria H2.
        pedidoRepository.saveAll(List.of(
                pedidoVencidoCliente10,
                pedidoConfirmadoCliente10,
                pedidoVencidoCliente20
        ));
    }

    @Test
    void findByClienteId_DebeRetornarSoloLosPedidosDelClienteIndicado() {

        // When: buscamos todos los pedidos pertenecientes al cliente 10.
        List<Pedido> pedidosCliente10 = pedidoRepository.findByClienteId(10);

        // Then: debe encontrar exactamente los 2 pedidos del cliente 10 y ninguno del cliente 20.
        assertEquals(2, pedidosCliente10.size());
        assertTrue(pedidosCliente10.stream().allMatch(p -> p.getClienteId().equals(10)));

    }

    @Test
    void findByEstadoAndFechaCreacionBefore_DebeRetornarSoloPedidosCreadosAnterioresAlLimite() {

        // Given: limite de antiguedad de 24 horas hacia atras (regla de expiración del batch).
        LocalDateTime fechaLimite = ahora.minusHours(24);

        // When: consultamos la primera página (tamaño 10) de pedidos CREADO anteriores al límite.
        Page<Pedido> paginaVencidos = pedidoRepository.findByEstadoAndFechaCreacionBefore(
                EstadoPedido.CREADO,
                fechaLimite,
                PageRequest.of(0, 10)
        );

        // Then: deben aparecer unicamente los 2 pedidos en estado CREADO con más de 24hs (excluyendo el CONFIRMADO que tiene 2 horas).
        assertEquals(2, paginaVencidos.getTotalElements());
        assertTrue(paginaVencidos.getContent().stream()
                .allMatch(p -> p.getEstado() == EstadoPedido.CREADO && p.getFechaCreacion().isBefore(fechaLimite)));

    }

}
