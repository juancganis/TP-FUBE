package com.tiendaonline.clientes.dto;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record ItemPedidoDto (
     Integer itemPedidoId,
     Integer productoId,
     Integer cantidad,
     BigDecimal precioUnitario
) {}
