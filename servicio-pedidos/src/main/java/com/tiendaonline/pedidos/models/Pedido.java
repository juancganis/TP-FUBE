package com.tiendaonline.pedidos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

import com.tiendaonline.pedidos.enums.EstadoPedido;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    private int id; // todo: FALTAN ANOTACIONES DE AUTOINCREMENTAL.
    private int clienteId;
    private EstadoPedido estado;
    private double montoTotal;
    private LocalDate fechaCreacion;
    private LocalDate fechaActualizacion;

}
