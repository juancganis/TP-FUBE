package models;

import enums.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    private int id; // todo: FALTAN ANOTACIONES DE AUTOINCREMENTAL.
    private Producto producto;
    private Pedido pedido; // todo: AGREGAR DEPENDENCIA DE PEDIDO.
    private int cantidad;
    private EstadoReserva estado;

}
