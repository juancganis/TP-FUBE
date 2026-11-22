package com.tiendaonline.inventario.models;

import com.tiendaonline.inventario.enums.EstadoReserva;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int pedidoId;
    private int cantidad;
    private LocalDateTime fechaCreacion;

    @Enumerated (EnumType.STRING) // para que guarde literalmente el string y no un numero que le corresponde al valor del enum
    private EstadoReserva estado;

    @ManyToOne // relacion uno a muchos (un producto, muchas reservas)
    @JoinColumn(name = "producto_id") // le digo que columna tiene que mirar para buscarlo
    private Producto producto;
}
