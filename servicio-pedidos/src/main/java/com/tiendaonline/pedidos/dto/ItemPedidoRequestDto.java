package com.tiendaonline.pedidos.dto;

import lombok.Builder;

@Builder
public record ItemPedidoRequestDto(
     String sku,
     Integer cantidad
) {}
