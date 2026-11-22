package com.tiendaonline.inventario.dto;

import com.tiendaonline.inventario.enums.EstadoReserva;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservaResponseDto(
        Integer id,
        Integer pedidoId,
        int cantidad,
        LocalDateTime fechaCreacion,
        EstadoReserva estado,
        String sku
){}
