package com.tiendaonline.pedidos.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.tiendaonline.pedidos.client.ClienteClient;
import com.tiendaonline.pedidos.client.InventarioClient;
import com.tiendaonline.pedidos.dto.*;
import com.tiendaonline.pedidos.enums.EstadoPedido;
import org.springframework.transaction.annotation.Transactional;
import com.tiendaonline.pedidos.exception.RecursoNoEncontradoException;
import com.tiendaonline.pedidos.exception.EstadoPedidoInvalidoException;

import com.tiendaonline.pedidos.mapper.PedidoMapper;
import com.tiendaonline.pedidos.models.ItemPedido;
import com.tiendaonline.pedidos.models.Pedido;
import com.tiendaonline.pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;


@Service
public class PedidoServiceImpl implements PedidoService{

    private final PedidoRepository pedidoRepository;
    private final ClienteClient clienteClient;
    private final InventarioClient inventarioClient;

    // Hacemos la inyección de dependencias por constructor:
    public PedidoServiceImpl(PedidoRepository pedidoRepository, ClienteClient clienteClient, InventarioClient inventarioClient) {
        this.pedidoRepository = pedidoRepository;
        this.clienteClient = clienteClient;
        this.inventarioClient = inventarioClient;
    }

    @Override

    public PedidoResponseDto crearPedido(PedidoRequestDto requestDto) {

        return null;
    }

    @Override
    public PedidoResponseDto obtenerPedidoPorId(Integer pedidoId) {

        return null;
    }

    @Override
    public List<PedidoResponseDto> listarPedidosPorCliente(Integer clienteId) {
        return null;
    }

    @Override
    @Transactional // Con esto, nos aseguramos de que el cambio de estado a CANCELADO solo se confirme en la base de datos
                   // si la liberación de stock en inventario fue exitosa.
    public PedidoResponseDto cancelarPedido(Integer pedidoId) {
        // Buscamos el pedido en base de datos:
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el pedido con ID: " + pedidoId));

        // Validamos que el estado actual permita la cancelación (solo CREADO o CONFIRMADO):
        if (pedido.getEstado() == EstadoPedido.CANCELADO || pedido.getEstado() == EstadoPedido.EXPIRADO) {
            throw new EstadoPedidoInvalidoException("No se puede cancelar un pedido en estado: " + pedido.getEstado());
        }

        // Notificamos a servicio-inventario para liberar el stock reservado de este pedido:
        inventarioClient.liberarReservasPorPedido(pedidoId);

        // Actualizar estado a CANCELADO y persistir:
        pedido.setEstado(EstadoPedido.CANCELADO);
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        return PedidoMapper.toDTO(pedidoActualizado);
    }

}
