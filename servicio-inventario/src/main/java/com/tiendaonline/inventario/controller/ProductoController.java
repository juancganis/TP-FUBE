package com.tiendaonline.inventario.controller;

import com.tiendaonline.inventario.service.ProductoService;

public class ProductoController {

    private ProductoService productoService;

    public ProductoController(ProductoService productoService1){
        this.productoService = productoService1;
    }
}
