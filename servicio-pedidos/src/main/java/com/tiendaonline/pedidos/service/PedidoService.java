package com.tiendaonline.pedidos.service;

import java.util.List;
import com.tiendaonline.pedidos.dto.PedidoRequestDto;
import com.tiendaonline.pedidos.dto.PedidoResponseDto;

public interface PedidoService {

    PedidoResponseDto crearPedido(PedidoRequestDto requestDto);
    PedidoResponseDto obtenerPedidoPorId(Integer pedidoId);
    List<PedidoResponseDto> listarPedidosPorCliente(Integer clienteId);
    PedidoResponseDto cancelarPedido(Integer pedidoId);

}
