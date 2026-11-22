package com.tiendaonline.pedidos.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record PedidoRequestDto(
     Integer clienteId,
     List<ItemPedidoRequestDto> listaItemsPedido
){}
