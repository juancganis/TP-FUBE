package com.tiendaonline.inventario.dto;

import lombok.Builder;

@Builder
public record ProductoDto (
        Integer id,
        String nombre,
        String sku,
        int cantidadDisponible
){}
