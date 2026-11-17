package com.tiendaonline.inventario.mapper;

import com.tiendaonline.inventario.dto.ProductoDto;
import com.tiendaonline.inventario.models.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoDto toDto (Producto producto) {
        return new ProductoDto(
                producto.getId(),
                producto.getNombre(),
                producto.getSku(),
                producto.getCantidadDisponible()
        );
    }

    public Producto toEntity (ProductoDto productoDto){
        return new Producto(
                productoDto.id(),
                productoDto.nombre(),
                productoDto.sku(),
                productoDto.cantidadDisponible()
        );
    }
}
