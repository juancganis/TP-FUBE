package com.tiendaonline.clientes.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.tiendaonline.clientes.dto.ClienteDto;
import com.tiendaonline.clientes.exception.ClienteExistenteException;
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
        LocalDate fechaDeAlta = LocalDate.now();
        Cliente cliente = new Cliente(clienteDto.nombre(), clienteDto.email(), fechaDeAlta);

        validarClienteExistente(clienteDto);
        
        clienteRepository.save(cliente);
        return clienteDto;
    }

    @Override 
    public ClienteDto consultarClientePorId(Integer id) {
        Cliente cliente = clienteRepository.getReferenceById(id);
        ClienteDto clienteDto = new ClienteDto(cliente.getNombre(), cliente.getEmail());
        return clienteDto;
    }

    public void validarClienteExistente(ClienteDto clienteDto) throws ClienteExistenteException {
        if(clienteRepository.existsByEmail(clienteDto.email())) {
            throw new ClienteExistenteException("Ya existe un cliente con ese email");
        }
    }
}
