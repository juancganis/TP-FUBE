package com.tiendaonline.inventario.controller;

import com.tiendaonline.inventario.dto.ProductoResponseDto;
import com.tiendaonline.inventario.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService1){
        this.productoService = productoService1;
    }

    @GetMapping("/{sku}/stock")
    public ResponseEntity<ProductoResponseDto> consultarStock(@PathVariable String sku){
        ProductoResponseDto producto = productoService.consultarStock(sku);
        return ResponseEntity.ok(producto);
    }
}
