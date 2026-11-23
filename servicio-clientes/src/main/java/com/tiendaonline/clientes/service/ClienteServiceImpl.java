package com.tiendaonline.clientes.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tiendaonline.clientes.client.PedidosClient;
import com.tiendaonline.clientes.dto.ClienteDto;
import com.tiendaonline.clientes.dto.PedidoDto;
import com.tiendaonline.clientes.exception.ClienteExistenteException;
import com.tiendaonline.clientes.exception.ClienteNoExistentePorIdException;
import com.tiendaonline.clientes.models.Cliente;
import com.tiendaonline.clientes.repository.ClienteRepository;

@Service 
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;
    private final FeatureFlagService featureFlagService;
    private final PedidosClient pedidosClient;

    public ClienteServiceImpl(ClienteRepository clienteRepository, FeatureFlagService featureFlagService, PedidosClient pedidosClient) {
        this.clienteRepository = clienteRepository;
        this.featureFlagService = featureFlagService;
        this.pedidosClient = pedidosClient;
    }

    @Override 
    public ClienteDto registrarCliente(ClienteDto clienteDto) {
        LocalDateTime fechaDeAlta = LocalDateTime.now();
        Cliente cliente = new Cliente(clienteDto.nombre(), clienteDto.email(), fechaDeAlta);

        validarClienteExistente(clienteDto);
        
        clienteRepository.save(cliente);
        return clienteDto;
    }

    @Override
    public ClienteDto consultarClientePorId(Integer id) {
        Cliente cliente = buscarClientePorId(id);

        if (featureFlagService.isEnabled("mask-pii", String.valueOf(id))) {
            return new ClienteDto(cliente.nombreEnmascarado(), cliente.emailEnmascarado());
        }
        return new ClienteDto(cliente.getNombre(), cliente.getEmail());
    }

    @Override 
    public List<PedidoDto> consultarPedidosCliente(Integer id) {
        buscarClientePorId(id);
        return pedidosClient.consultarPedidosCliente(id);
    }

    private void validarClienteExistente(ClienteDto clienteDto) throws ClienteExistenteException {
        if(clienteRepository.existsByEmail(clienteDto.email())) {
            throw new ClienteExistenteException("Ya existe un cliente con ese email");
        }
    }

    private Cliente buscarClientePorId(Integer id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new ClienteNoExistentePorIdException("El cliente no existe"));
    }
}
