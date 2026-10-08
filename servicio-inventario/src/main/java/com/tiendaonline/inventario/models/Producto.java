package com.tiendaonline.inventario.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    private int id; // todo: FALTAN ANOTACIONES DE AUTOINCREMENTAL.
    private String nombre;
    private int cantidadDisponible;

}
