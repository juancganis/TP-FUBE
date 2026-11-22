package com.tiendaonline.inventario.mapper;

import com.tiendaonline.inventario.dto.ReservaDto;
import com.tiendaonline.inventario.models.Producto;
import com.tiendaonline.inventario.models.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    public ReservaDto toDto (Reserva reserva){
        return ReservaDto.builder().
                id(reserva.getId()).
                pedidoId(reserva.getPedidoId()).
                cantidad(reserva.getCantidad()).
                fechaCreacion(reserva.getFechaCreacion()).
                estado(reserva.getEstado()).productoId(reserva.getProducto().getId()).build();
    }

    public Reserva toEntity (ReservaDto reservaDto, Producto producto){ // decidí pasarle directamente el producto así le cede la responsabilidad de buscar el producto más arriba
        return Reserva.builder().
                id(reservaDto.id()).
                pedidoId(reservaDto.pedidoId()).
                cantidad(reservaDto.cantidad()).
                fechaCreacion(reservaDto.fechaCreacion()).
                estado(reservaDto.estado()).
                producto(producto).build();
    }
}
