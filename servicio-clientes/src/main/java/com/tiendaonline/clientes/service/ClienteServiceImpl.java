package com.tiendaonline.clientes.service;

import org.springframework.stereotype.Service;

import com.tiendaonline.clientes.dto.ClienteDto;
import com.tiendaonline.clientes.models.Cliente;
import com.tiendaonline.clientes.repository.ClienteRepository;

@Service 
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override 
    public ClienteDto registrarCliente(ClienteDto clienteDto) {
        Cliente cliente = new Cliente(clienteDto.nombre(), clienteDto.email(), clienteDto.fechaAlta());
        clienteRepository.save(cliente);
        return clienteDto;
    }
}
