package com.tiendaonline.inventario.service;

import com.tiendaonline.inventario.dto.ProductoDto;

public interface ProductoService {
    ProductoDto consultarStock(String sku);
}
