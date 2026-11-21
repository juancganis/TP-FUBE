package com.tiendaonline.pedidos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.tiendaonline.pedidos.enums.EstadoPedido;

public record PedidoResponseDto(
     Integer pedidoId,
     Integer clienteId,
     EstadoPedido estado,
     BigDecimal montoTotal,
     LocalDateTime fechaCreacion,
     LocalDateTime fechaActualizacion,
     List<ItemPedidoResponseDto> listaItemsPedido
) {}
