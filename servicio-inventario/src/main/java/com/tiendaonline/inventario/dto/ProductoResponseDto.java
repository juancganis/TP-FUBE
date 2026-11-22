package com.tiendaonline.inventario.dto;

import lombok.Builder;

@Builder
public record ProductoResponseDto(
        Integer id,
        String nombre,
        String sku,
        int cantidadDisponible
){}
