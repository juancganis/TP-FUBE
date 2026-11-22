package com.tiendaonline.inventario.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductoResponseDto(
        Integer id,
        String nombre,
        String sku,
        int cantidadDisponible,
        BigDecimal precioUnitario
){}
