package com.tiendaonline.pedidos.dto;

import java.math.BigDecimal;

public record ProductoResponseDto (
        Integer id,
        String nombre,
        String sku,
        Integer cantidadDisponible,
        BigDecimal precioUnitario
)

{}
