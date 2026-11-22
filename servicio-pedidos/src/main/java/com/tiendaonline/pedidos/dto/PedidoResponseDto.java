package com.tiendaonline.pedidos.dto;

import lombok.Builder;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.tiendaonline.pedidos.enums.EstadoPedido;

@Builder
public record PedidoResponseDto(
     Integer pedidoId,
     Integer clienteId,
     EstadoPedido estado,
     BigDecimal montoTotal,
     LocalDateTime fechaCreacion,
     LocalDateTime fechaActualizacion,
     List<ItemPedidoResponseDto> listaItemsPedido
) {}
