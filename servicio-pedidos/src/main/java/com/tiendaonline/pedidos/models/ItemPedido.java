package com.tiendaonline.pedidos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    private int id; // todo: FALTAN ANOTACIONES DE AUTOINCREMENTAL.
    private Pedido pedido;
    private int idProducto; // todo: AGREGAR DEPENDENCIA DE PRODUCTO.
    private int cantidad;
    private double precioUnitario;

}
