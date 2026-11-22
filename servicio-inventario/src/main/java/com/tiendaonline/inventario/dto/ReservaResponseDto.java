package com.tiendaonline.inventario.dto;

import com.tiendaonline.inventario.enums.EstadoReserva;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservaResponseDto(
        Integer pedidoId,
        int cantidad,
        LocalDateTime fechaCreacion,
        String sku
){}
