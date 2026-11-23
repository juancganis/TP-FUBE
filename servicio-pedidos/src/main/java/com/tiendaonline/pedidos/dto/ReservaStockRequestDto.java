package com.tiendaonline.pedidos.dto;

import lombok.Builder;
import java.time.LocalDateTime;

// DTO interno que usamos para enviar la solicitud de reserva de stock a servicio-inventario.
@Builder
public record ReservaStockRequestDto(
        Integer pedidoId,
        Integer cantidad,
        LocalDateTime fechaCreacion,
        String sku
) {}
