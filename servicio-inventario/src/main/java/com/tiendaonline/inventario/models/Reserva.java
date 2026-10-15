package com.tiendaonline.inventario.models;

import com.tiendaonline.inventario.enums.EstadoReserva;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    private int id; // todo: FALTAN ANOTACIONES DE AUTOINCREMENTAL.
    private Producto producto;
    private int idPedido; // todo: AGREGAR DEPENDENCIA DE PEDIDO.
    private int cantidad;
    private EstadoReserva estado;

}
