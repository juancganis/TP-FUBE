package com.tiendaonline.pedidos.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDto (
     Integer itemPedidoId,
     Integer productoId,
     int cantidad,
     BigDecimal precioUnitario
) {}
