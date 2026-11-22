package com.tiendaonline.inventario.mapper;

import com.tiendaonline.inventario.dto.ProductoResponseDto;
import com.tiendaonline.inventario.models.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    // metodo de clase para pasar una clase de modelo a dto
    public static ProductoResponseDto toDto (Producto producto) {
        return ProductoResponseDto.builder().
                id(producto.getId()).
                nombre(producto.getNombre()).
                sku(producto.getSku()).
                cantidadDisponible(producto.getCantidadDisponible()).build();
    }

    // metodo de clase para pasar una clase dto a modelo
    public static Producto toEntity (ProductoResponseDto productoResponseDto){
        return Producto.builder().
                id(productoResponseDto.id()).
                nombre(productoResponseDto.nombre()).
                sku(productoResponseDto.sku()).
                cantidadDisponible(productoResponseDto.cantidadDisponible()).build();
    }
}