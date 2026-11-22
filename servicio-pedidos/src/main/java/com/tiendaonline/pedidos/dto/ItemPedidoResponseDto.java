package com.tiendaonline.pedidos.dto;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record ItemPedidoResponseDto (
     Integer itemPedidoId,
     Integer productoId,
     Integer cantidad,
     BigDecimal precioUnitario
) {}
