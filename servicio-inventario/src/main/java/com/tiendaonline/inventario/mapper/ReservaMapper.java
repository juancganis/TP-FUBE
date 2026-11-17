package com.tiendaonline.inventario.mapper;

import com.tiendaonline.inventario.dto.ReservaDto;
import com.tiendaonline.inventario.models.Producto;
import com.tiendaonline.inventario.models.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    public ReservaDto toDto (Reserva reserva){
        return new ReservaDto(
                reserva.getId(),
                reserva.getIdPedido(),
                reserva.getCantidad(),
                reserva.getFechaCreacion(),
                reserva.getEstado(),
                reserva.getProducto().getId());
    }

    public Reserva toEntity (ReservaDto reservaDto, Producto producto){ // decidí pasarle directamente el producto así le cede la responsabilidad de buscar el producto más arriba
        return new Reserva(
                reservaDto.id(),
                reservaDto.idPedido(),
                reservaDto.cantidad(),
                reservaDto.fechaCreacion(),
                reservaDto.estado(),
                producto
        );
    }
}
