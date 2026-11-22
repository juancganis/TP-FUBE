package com.tiendaonline.inventario.service;

import com.tiendaonline.inventario.dto.ProductoResponseDto;

public interface ProductoService {
    ProductoResponseDto consultarStock(String sku);
    ProductoResponseDto guardarEstado(ProductoResponseDto productoResponseDto);
}
