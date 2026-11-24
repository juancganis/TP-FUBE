package com.tiendaonline.pedidos.dto;

import lombok.Builder;
import java.math.BigDecimal;

// DTO interno que usamos para recibir el producto pedido a servicio-inventario.
@Builder
public record ProductoResponseDto (
        Integer id,
        String nombre,
        String sku,
        Integer cantidadDisponible,
        BigDecimal precioUnitario
)

{}
