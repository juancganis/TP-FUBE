package com.tiendaonline.inventario.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.Entity; // le digo que esta es una entidad de la bdd
import jakarta.persistence.GeneratedValue; // te deja poner la estrategia para generar id
import jakarta.persistence.GenerationType; // enum que contiene las estrategias para el GeneratedValue
import jakarta.persistence.Id; // marca al id

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String sku;
    private int cantidadDisponible;

}
