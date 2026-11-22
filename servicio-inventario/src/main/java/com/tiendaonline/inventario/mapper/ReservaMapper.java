package com.tiendaonline.inventario.mapper;

import com.tiendaonline.inventario.dto.ReservaResponseDto;
import com.tiendaonline.inventario.models.Producto;
import com.tiendaonline.inventario.models.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    public ReservaResponseDto toDto (Reserva reserva){
        return ReservaResponseDto.builder().
                id(reserva.getId()).
                pedidoId(reserva.getPedidoId()).
                cantidad(reserva.getCantidad()).
                fechaCreacion(reserva.getFechaCreacion()).
                estado(reserva.getEstado()).sku(reserva.getProducto().getSku()).build();
    }

    public Reserva toEntity (ReservaResponseDto reservaDto, Producto producto){ // decidí pasarle directamente el producto así le cede la responsabilidad de buscar el producto más arriba
        return Reserva.builder().
                id(reservaDto.id()).
                pedidoId(reservaDto.pedidoId()).
                cantidad(reservaDto.cantidad()).
                fechaCreacion(reservaDto.fechaCreacion()).
                estado(reservaDto.estado()).
                producto(producto).build();
    }
}
