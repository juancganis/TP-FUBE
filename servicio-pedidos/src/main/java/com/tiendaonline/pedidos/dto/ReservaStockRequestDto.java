package com.tiendaonline.pedidos.dto;

import java.time.LocalDateTime;

// DTO interno utilizado para enviar la solicitud de reserva de stock a servicio-inventario:
public record ReservaStockRequestDto(
        Integer pedidoId,
        Integer cantidad,
        LocalDateTime fechaCreacion,
        String sku
) {}
