package com.tiendaonline.clientes.service;

import java.util.List;

import com.tiendaonline.clientes.dto.ClienteDto;
import com.tiendaonline.clientes.dto.PedidoDto;

public interface ClienteService {
    ClienteDto registrarCliente(ClienteDto clienteDto);
    ClienteDto consultarClientePorId(Integer id);
    List<PedidoDto> consultarPedidosCliente(Integer id);
}
