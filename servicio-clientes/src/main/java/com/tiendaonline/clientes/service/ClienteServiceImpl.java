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
import lombok.extern.slf4j.Slf4j;

@Service 
@Slf4j
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
        log.info("Iniciando registro de cliente");
        LocalDateTime fechaDeAlta = LocalDateTime.now();
        Cliente cliente = new Cliente(clienteDto.nombre(), clienteDto.email(), fechaDeAlta);

        validarClienteExistente(clienteDto);
        
        Cliente clienteGuardado = clienteRepository.save(cliente);
        log.info("Se registro un cliente con id = {}", clienteGuardado.getId());
        return clienteDto;
    }

    @Override
    public ClienteDto consultarClientePorId(Integer id) {
        log.info("Consultando cliente con id = {}", id);
        Cliente cliente = buscarClientePorId(id);

        boolean flag = featureFlagService.isEnabled("mask-pii", String.valueOf(id));
        log.debug("Feature flag mask-pii = {} para cliente con id = {}", flag, id);

        if (flag) {
            return new ClienteDto(cliente.nombreEnmascarado(), cliente.emailEnmascarado());
        }
        return new ClienteDto(cliente.getNombre(), cliente.getEmail());
    }

    @Override 
    public List<PedidoDto> consultarPedidosCliente(Integer id) {
        log.info("Consultando pedidos del cliente con id = {}", id);
        buscarClientePorId(id);

        List<PedidoDto> pedidos = pedidosClient.consultarPedidosCliente(id);
        log.info("Se obtuvieron {} pedidos del cliente con id = {}", pedidos.size(), id);

        return pedidos;
    }

    private void validarClienteExistente(ClienteDto clienteDto) throws ClienteExistenteException {
        if(clienteRepository.existsByEmail(clienteDto.email())) {
            log.warn("Excepcion Cliente Existente: ya existe un cliente con ese email");
            throw new ClienteExistenteException("Ya existe un cliente con ese email");
        }
    }

    private Cliente buscarClientePorId(Integer id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Excepcion Cliente No Existente: el cliente con id = {} no existe", id);
                return new ClienteNoExistentePorIdException("El cliente no existe");
            });
    }
}
