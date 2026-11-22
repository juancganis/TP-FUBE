package com.tiendaonline.inventario.service;

import com.tiendaonline.inventario.dto.ProductoResponseDto;
import com.tiendaonline.inventario.dto.ReservaResponseDto;
import com.tiendaonline.inventario.enums.EstadoReserva;
import com.tiendaonline.inventario.exception.StockInsuficiente;
import com.tiendaonline.inventario.mapper.ProductoMapper;
import com.tiendaonline.inventario.mapper.ReservaMapper;
import com.tiendaonline.inventario.models.Reserva;
import com.tiendaonline.inventario.repository.ReservaRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ProductoServiceImpl productoService;

    public ReservaServiceImpl(ReservaRepository reservaRepository, ProductoServiceImpl productoService){
        this.reservaRepository = reservaRepository;
        this.productoService = productoService;
    }

    @Override
    public ReservaResponseDto crearReserva(ReservaResponseDto reservaResponseDto) {
        // reviso que exista el producto
        ProductoResponseDto producto = productoService.consultarStock(reservaResponseDto.sku());

        // reviso que alcance el stock para la reserva
        if(reservaResponseDto.cantidad()>producto.cantidadDisponible()){
            throw new StockInsuficiente("No alcanza el stock del producto "+ producto.nombre());
        }
        // si pasó significa que tiene stock

        // ahora actualizo el estado del producto en la bdd
        // tengo que crear un nuevo dto porque public record es final los atributos
        ProductoResponseDto productoModificado = ProductoResponseDto.builder().
                id(producto.id()).
                nombre(producto.nombre()).
                sku(producto.sku()).
                cantidadDisponible(producto.cantidadDisponible()-reservaResponseDto.cantidad()).build();

        productoService.guardarEstado(productoModificado);

        // guardo la reserva en la bdd
        Reserva reserva = new ReservaMapper().toEntity(reservaResponseDto, ProductoMapper.toEntity(productoModificado));
        reserva.setEstado(EstadoReserva.ACTIVA);

        reservaRepository.save(reserva);

        return new ReservaMapper().toDto(reserva);
    }

    @Override
    public void liberarReserva(Integer idPedido) {

    }
}
