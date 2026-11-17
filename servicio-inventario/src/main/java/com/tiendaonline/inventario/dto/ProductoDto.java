package com.tiendaonline.inventario.dto;

public record ProductoDto (
        Integer id,
        String nombre,
        String sku,
        int cantidadDisponible
){}
