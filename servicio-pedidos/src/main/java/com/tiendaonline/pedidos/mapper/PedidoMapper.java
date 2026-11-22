package com.tiendaonline.pedidos.mapper;

import java.util.List;
import java.util.Objects;

import com.tiendaonline.pedidos.dto.ItemPedidoRequestDto;
import com.tiendaonline.pedidos.dto.ItemPedidoResponseDto;
import com.tiendaonline.pedidos.dto.PedidoRequestDto;
import com.tiendaonline.pedidos.dto.PedidoResponseDto;
import com.tiendaonline.pedidos.models.ItemPedido;
import com.tiendaonline.pedidos.models.Pedido;

public class PedidoMapper {

    // Metodo PÚBLICO de clase para pasar una clase DTO del tipo "PedidoRequestDto" a la entidad "Pedido":
    public static Pedido toEntity(PedidoRequestDto pedidoRequestDto){

        // Si el pedidoRequestDto es null, devolvemos null y evitamos una NullPointerException.
        if(pedidoRequestDto == null) return null;

        List<ItemPedidoRequestDto> itemsPedidoRequestDto = pedidoRequestDto.listaItemsPedido();

        // Convertimos cada DTO de ítem en una entidad ItemPedido usando el metodo auxiliar y agrupamos el resultado en una lista.
        // Nos aseguramos de filtrar por aquellos itemsPedidoRequestDto que no sean nulos.
        // Si la lista es nula,se asigna List.of() para que en el JSON de respuesta viaje un arreglo vacío ([]) en lugar de null.
        List<ItemPedido> listaAuxilarItemsPedido = (itemsPedidoRequestDto != null) ? itemsPedidoRequestDto.stream().filter(Objects::nonNull).map(PedidoMapper::toItemEntity).toList() : List.of();

        Pedido pedidoDevuelto = Pedido.builder()
                .clienteId(pedidoRequestDto.clienteId())
                .listaItemsPedido(listaAuxilarItemsPedido)
                .build();

        // Asignamos a cada itemPedido el Pedido que le corresponde:
        pedidoDevuelto.getListaItemsPedido().forEach(itemPedido -> itemPedido.setPedido(pedidoDevuelto));

        return pedidoDevuelto;

    }

    // Metodo PÚBLICO de clase para pasar una entidad del tipo "Pedido" a la clase DTO del tipo "PedidoResponseDto":
    public static PedidoResponseDto toDTO (Pedido pedido){

        // Si el pedido es null, devolvemos null y evitamos una NullPointerException.
        if(pedido == null) return null;

        List<ItemPedido> itemsPedido = pedido.getListaItemsPedido();

        // Convertimos cada entidad de ítem en un Dto de ItemPedido usando el metodo auxiliar y agrupamos el resultado en una lista.
        // Nos aseguramos de filtrar por aquellos itemsPedido que no sean nulos:
        // Si la lista es nula,se asigna List.of() para que en el JSON de respuesta viaje un arreglo vacío ([]) en lugar de null.
        List<ItemPedidoResponseDto> itemsDto = (itemsPedido != null) ? itemsPedido.stream().filter(Objects::nonNull).map(PedidoMapper::toItemDTO).toList() : List.of();

        return PedidoResponseDto.builder()
                .pedidoId(pedido.getId())
                .clienteId(pedido.getClienteId())
                .estado(pedido.getEstado())
                .montoTotal(pedido.getMontoTotal())
                .fechaCreacion(pedido.getFechaCreacion())
                .fechaActualizacion(pedido.getFechaActualizacion())
                .listaItemsPedido(itemsDto)
                .build();

    }

    /* Métodos auxiliares privados: encapsulan la conversión individual de cada ítem,
       ya que un ItemPedido nunca se mapea de forma independiente fuera de un Pedido. */

    // Metodo PRIVADO de clase para pasar una clase DTO del tipo "ItemPedidoRequestDto" a la entidad "ItemPedido":
    private static ItemPedido toItemEntity(ItemPedidoRequestDto itemPedidoRequestDto){

        if(itemPedidoRequestDto == null) return null;

        return ItemPedido.builder()
                .productoId(itemPedidoRequestDto.productoId())
                .cantidad(itemPedidoRequestDto.cantidad())
                .build();

    }

    // Metodo PRIVADO de clase para pasar una entidad del tipo "ItemPedido" a la clase DTO del tipo "ItemPedidoResponseDto":
    private static ItemPedidoResponseDto toItemDTO(ItemPedido itemPedido){

        if(itemPedido == null) return null;

        return ItemPedidoResponseDto.builder()
                .itemPedidoId(itemPedido.getId())
                .productoId(itemPedido.getProductoId())
                .cantidad(itemPedido.getCantidad())
                .precioUnitario(itemPedido.getPrecioUnitario())
                .build();

    }

}
