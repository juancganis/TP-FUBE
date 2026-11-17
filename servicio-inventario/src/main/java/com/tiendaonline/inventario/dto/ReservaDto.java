package com.tiendaonline.inventario.dto;


import com.tiendaonline.inventario.enums.EstadoReserva;

import java.time.LocalDateTime;

public record ReservaDto (
        Integer id,
        int idPedido,
        int cantidad,
        LocalDateTime fechaCreacion,
        EstadoReserva estado,
        Integer idProducto
){}
