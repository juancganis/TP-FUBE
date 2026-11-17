package com.tiendaonline.inventario.models;

import com.tiendaonline.inventario.enums.EstadoReserva;

import jakarta.persistence.Enumerated; // le digo que es un enum
import jakarta.persistence.EnumType; // le digo como quiero que guarde el enum
import jakarta.persistence.ManyToOne; // para modelar la relacion varios -> uno
import jakarta.persistence.JoinColumn; // le digo en que columna buscar
import jakarta.persistence.Entity; // le digo que esta es una entidad de la bdd
import jakarta.persistence.GeneratedValue; // te deja poner la estrategia para generar id
import jakarta.persistence.GenerationType; // enum que contiene las estrategias para el GeneratedValue (IDENTITY la de la bdd)
import jakarta.persistence.Id; // marca al id
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int idPedido;
    private int cantidad;
    private LocalDateTime fechaCreacion;

    @Enumerated (EnumType.STRING) // para que guarde literalmente el string y no un numero que le corresponde al valor del enum
    private EstadoReserva estado;

    @ManyToOne // relacion uno a muchos (un producto, muchas reservas)
    @JoinColumn(name = "producto_id") // le digo que columna tiene que mirar para buscarlo
    private Producto producto;
}
