package com.tiendaonline.inventario.service;

import com.tiendaonline.inventario.dto.ProductoResponseDto;
import com.tiendaonline.inventario.exception.ProductoNoEncontrado;
import com.tiendaonline.inventario.mapper.ProductoMapper;
import com.tiendaonline.inventario.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j // para los logs
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    // constructor al que se le pasa el repositorio de producto
    public ProductoServiceImpl(ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
    }

    @Override
    public ProductoResponseDto consultarStock(String sku) {
        // busco en el repositorio por sku y este me retorna un Producto que tengo que pasar a dto
        ProductoResponseDto respuesta = productoRepository.findBySku(sku).
                map(ProductoMapper::toDto).
                orElseThrow(()->new ProductoNoEncontrado("Producto no encontrado con código " + sku));

        log.info("Se buscó el producto con sku "+sku);

        return respuesta;
    }


    // este metodo es para separar bien el service de producto con reserva
    @Override
    public ProductoResponseDto guardarEstado(ProductoResponseDto productoResponseDto) {
        productoRepository.save(ProductoMapper.toEntity(productoResponseDto));

        log.info("se actualizó el valor del producto "+ productoResponseDto.nombre());

        return productoResponseDto;
    }

}
