package com.tiendaonline.inventario.service;

import com.tiendaonline.inventario.controller.ProductoController;
import com.tiendaonline.inventario.dto.ProductoDto;
import com.tiendaonline.inventario.mapper.ProductoMapper;
import com.tiendaonline.inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    // constructor al que se le pasa el repositorio de producto
    public ProductoServiceImpl(ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
    }

    @Override
    public ProductoDto consultarStock(String sku) {
        // busco en el repositorio por sku y este me retorna un Producto que tengo que pasar a dto
        return productoRepository.findBySku(sku).
                map(ProductoMapper::toDto).
                orElseThrow(()->new RuntimeException("Producto no encontrado con código " + sku));
    }
}
