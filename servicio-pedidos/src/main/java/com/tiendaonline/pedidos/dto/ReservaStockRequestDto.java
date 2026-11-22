package com.tiendaonline.pedidos.dto;

// DTO interno utilizado para enviar la solicitud de reserva de stock a servicio-inventario:
public record ReservaStockRequestDto(
        Integer pedidoId,
        String sku,
        Integer cantidad
) {}
