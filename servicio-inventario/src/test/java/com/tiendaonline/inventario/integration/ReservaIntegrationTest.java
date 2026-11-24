package com.tiendaonline.inventario.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiendaonline.inventario.dto.ReservaResponseDto;
import com.tiendaonline.inventario.enums.EstadoReserva;
import com.tiendaonline.inventario.models.Producto;
import com.tiendaonline.inventario.models.Reserva;
import com.tiendaonline.inventario.repository.ProductoRepository;
import com.tiendaonline.inventario.repository.ReservaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // levanta el contexto entero
@AutoConfigureMockMvc
@Transactional // para los test se asegura de hacer un rollback cuando terminan
public class ReservaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void vaciarBDD(){
        // limpio el repositorio antes de cada prueba
        reservaRepository.deleteAll();
        productoRepository.deleteAll();
    }

    @Test
    void crearReservaFlujoCompleto() throws Exception { // el Exception me lo pidió mockMvc.perform
        // GIVEN:
        // tengo que crear el producto y guardarlo
        Producto producto = Producto.builder().
                id(1).
                nombre("Remera - Negra - Talle XS").
                sku("REM-NE-XS").
                cantidadDisponible(12).
                precioUnitario(BigDecimal.valueOf(44000.00)).build();
        productoRepository.save(producto);

        // también hago la reserva que me llegaría por el body del endpoint
        ReservaResponseDto reservaAGuardar = ReservaResponseDto.builder().
                pedidoId(55).
                cantidad(7).
                fechaCreacion(LocalDateTime.of(2026, 10, 5, 10, 30)).
                sku("REM-NE-XS").build();

        // WHEN:
        // aca mando la llamada al endpoint con el dto de reserva en el body y espero un creado (201)
        mockMvc.perform(post("/v1/reservas").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(reservaAGuardar)))
                .andExpect(status().isCreated());

        // THEN:
        // primero chequeo que se haya bajado el stock
        Producto productoModificado = productoRepository.findBySku("REM-NE-XS").orElseThrow();

        assertEquals(5,productoModificado.getCantidadDisponible());

        // después miro que la reserva creada para el pedido tenga lo guardado
        List<Reserva> listaReservas = reservaRepository.findByPedidoIdAndEstado(55, EstadoReserva.ACTIVA);

        // primero reviso que solo haya 1 reserva en esa lista
        assertEquals(1,listaReservas.size());

        // luego valido los valores que contiene
        assertEquals(7, listaReservas.get(0).getCantidad());
        assertEquals(LocalDateTime.of(2026, 10, 5, 10, 30), listaReservas.get(0).getFechaCreacion());
    }
}
