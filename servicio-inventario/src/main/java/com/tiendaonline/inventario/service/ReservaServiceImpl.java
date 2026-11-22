package com.tiendaonline.inventario.service;

import com.tiendaonline.inventario.dto.ProductoResponseDto;
import com.tiendaonline.inventario.dto.ReservaResponseDto;
import com.tiendaonline.inventario.enums.EstadoReserva;
import com.tiendaonline.inventario.exception.ReservasNoEncontradas;
import com.tiendaonline.inventario.exception.StockInsuficiente;
import com.tiendaonline.inventario.mapper.ProductoMapper;
import com.tiendaonline.inventario.mapper.ReservaMapper;
import com.tiendaonline.inventario.models.Producto;
import com.tiendaonline.inventario.models.Reserva;
import com.tiendaonline.inventario.repository.ReservaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
        // reviso que exista el producto y miro el stock
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
                cantidadDisponible(producto.cantidadDisponible()-reservaResponseDto.cantidad()).
                precioUnitario(producto.precioUnitario()).build();

        productoService.guardarEstado(productoModificado);

        // guardo la reserva en la bdd
        Reserva reserva = new ReservaMapper().toEntity(reservaResponseDto, ProductoMapper.toEntity(productoModificado));
        reserva.setEstado(EstadoReserva.ACTIVA);

        reservaRepository.save(reserva);

        log.info("Se creó una reserva para el pedido "+ reserva.getPedidoId());

        return new ReservaMapper().toDto(reserva);
    }

    @Override
    public void liberarReserva(Integer pedidoId) {
        // recibo el id de un pedido y libero todas las reservas relacionadas al pedido
        // primero hago una lista de las reservas relacionadas al pedido que esten activas
        List<Reserva> reservasActivas = reservaRepository.findByPedidoIdAndEstado(pedidoId, EstadoReserva.ACTIVA);

        // si no se encontró ninguna reserva para ese pedido sale un error
        if(reservasActivas.isEmpty()){throw new ReservasNoEncontradas("No se encontraron reservas para dicho pedido");}

        // para cada reserva de reservasActivas le cambio el stock del producto que tiene
        // y la libero en la bdd
        for (Reserva reserva : reservasActivas){
            // cambio stock
            Producto producto = reserva.getProducto();

            producto.setCantidadDisponible(producto.getCantidadDisponible()+reserva.getCantidad());
            productoService.guardarEstado(ProductoMapper.toDto(producto));

            // actualizo el estado de la reserva y lo guardo
            reserva.setEstado(EstadoReserva.LIBERADA);
            reservaRepository.save(reserva);

            log.info("Se liberó la reserva "+reserva.getId());
        }

    }
}
