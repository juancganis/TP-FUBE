package com.tiendaonline.inventario.dto;

import lombok.Builder;

import com.tiendaonline.inventario.enums.EstadoReserva;

import java.time.LocalDateTime;

@Builder
public record ReservaDto (
        Integer id,
        int idPedido,
        int cantidad,
        LocalDateTime fechaCreacion,
        EstadoReserva estado,
        Integer idProducto
){}
