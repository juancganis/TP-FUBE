package com.tiendaonline.inventario.mapper;

import com.tiendaonline.inventario.dto.ReservaResponseDto;
import com.tiendaonline.inventario.models.Producto;
import com.tiendaonline.inventario.models.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    public ReservaResponseDto toDto (Reserva reserva){
        return ReservaResponseDto.builder().
                pedidoId(reserva.getPedidoId()).
                cantidad(reserva.getCantidad()).
                fechaCreacion(reserva.getFechaCreacion()).
                sku(reserva.getProducto().getSku()).build();
    }

    public Reserva toEntity (ReservaResponseDto reservaDto, Producto producto){
        // decidí pasarle directamente el producto así le cede la responsabilidad de buscar el producto más arriba
        // poner en null la reserva indica que es una entidad nueva que tiene que guardar en al bdd
        return Reserva.builder().
                id(null).
                pedidoId(reservaDto.pedidoId()).
                cantidad(reservaDto.cantidad()).
                fechaCreacion(reservaDto.fechaCreacion()).
                producto(producto).build();
    }
}
