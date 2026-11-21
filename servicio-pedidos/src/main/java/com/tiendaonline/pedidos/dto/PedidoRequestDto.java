package com.tiendaonline.pedidos.dto;

import java.util.List;

public record PedidoRequestDto(
     Integer clienteId,
     List<ItemPedidoRequestDto> listaItemsPedido
){}
