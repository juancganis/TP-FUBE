package com.tiendaonline.clientes.dto;

import lombok.Builder;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.tiendaonline.clientes.enums.EstadoPedido;

@Builder
public record PedidoDto(
     Integer pedidoId,
     Integer clienteId,
     EstadoPedido estado,
     BigDecimal montoTotal,
     LocalDateTime fechaCreacion,
     LocalDateTime fechaActualizacion,
     List<ItemPedidoDto> listaItemsPedido
) {}
