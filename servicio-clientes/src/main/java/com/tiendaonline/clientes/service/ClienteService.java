package com.tiendaonline.clientes.service;

import com.tiendaonline.clientes.dto.ClienteDto;

public interface ClienteService {
    ClienteDto registrarCliente(ClienteDto clienteDto);
    ClienteDto consultarClientePorId(Integer id);
    //ArrayList<Pedidos> consultarPedidosCliente(Integer id);
}
