package com.tiendaonline.inventario.service;


import com.tiendaonline.inventario.dto.ProductoResponseDto;
import com.tiendaonline.inventario.models.Producto;
import com.tiendaonline.inventario.repository.ProductoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceImplTest {

    // crea una copia falsa (mock) del repositorio
    @Mock
    ProductoRepository productoRepository;

    // crea una instancia real del service, pero las dependencias que usa son mocks
    @InjectMocks
    ProductoServiceImpl productoService;

    @Test
    void consultarStockExistente(){
        // Given: Planteamos el contexto inicial
        // creamos el producto falso
        Producto productoPrueba = Producto.builder().
                id(1).
                nombre("REMERA NEGRA").
                sku("001-RM-NE").
                cantidadDisponible(3).
                build();

        // When: Decimos qué es lo que pasa
        // le dice que cuando lo busquen con ese sku en el repository le devuelva el correspondiente
        Mockito.when(productoRepository.findBySku("001-RM-NE")).thenReturn(Optional.of(productoPrueba));

        // en este caso buscamos que el dto resultado dado por el service tenga lo que le pasamos originalmente
        ProductoResponseDto resultado = productoService.consultarStock("001-RM-NE");

        // Then: Resultado esperado
        // validamos que el nombre sea el que esperamos y que la cantidad también
        Assertions.assertEquals("REMERA NEGRA",resultado.nombre());
        Assertions.assertEquals("001-RM-NE",resultado.sku());
        Assertions.assertEquals(3,resultado.cantidadDisponible());
    }

    @Test
    void consultarStockDeProductoInexistente(){
        // Given: Planteamos el contexto inicial
        String sku = "sku falso";

        // When: Decimos qué es lo que pasa
        // cuando le llega un sku que no corresponda a un producto retorna vacío
        Mockito.when(productoRepository.findBySku(sku)).thenReturn(Optional.empty());

        // Then: Resultado esperado
        Assertions.assertThrows(RuntimeException.class, ()-> {
           productoService.consultarStock(sku);
        });
    }


}