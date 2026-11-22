package com.tiendaonline.pedidos.dto;

import lombok.Builder;

@Builder
public record ItemPedidoRequestDto(
     Integer productoId,
     Integer cantidad
) {}
