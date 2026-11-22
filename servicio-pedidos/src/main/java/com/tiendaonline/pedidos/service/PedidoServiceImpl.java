package com.tiendaonline.pedidos.service;

import java.util.List;

import com.tiendaonline.pedidos.client.ClienteClient;
import com.tiendaonline.pedidos.client.InventarioClient;
import com.tiendaonline.pedidos.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.tiendaonline.pedidos.dto.PedidoRequestDto;
import com.tiendaonline.pedidos.dto.PedidoResponseDto;

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
    @Transactional
    public PedidoResponseDto crearPedido(PedidoRequestDto requestDto){
        return null;
    }
    @Override
    public PedidoResponseDto obtenerPedidoPorId(Integer pedidoId){
        return null;
    }

    @Override
    public List<PedidoResponseDto> listarPedidosPorCliente(Integer clienteId){
        return null;
    }

    @Override
    public PedidoResponseDto cancelarPedido(Integer pedidoId){
        return null;
    }

}
