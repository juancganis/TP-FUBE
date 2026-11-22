package com.tiendaonline.inventario.mapper;

import com.tiendaonline.inventario.dto.ProductoDto;
import com.tiendaonline.inventario.models.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    // metodo de clase para pasar una clase de modelo a dto
    public static ProductoDto toDto (Producto producto) {
        return ProductoDto.builder().
                id(producto.getId()).
                nombre(producto.getNombre()).
                sku(producto.getSku()).
                cantidadDisponible(producto.getCantidadDisponible()).build();
    }

    // metodo de clase para pasar una clase dto a modelo
    public static Producto toEntity (ProductoDto productoDto){
        return Producto.builder().
                id(productoDto.id()).
                nombre(productoDto.nombre()).
                sku(productoDto.sku()).
                cantidadDisponible(productoDto.cantidadDisponible()).build();
    }
}