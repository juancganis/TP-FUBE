package com.tiendaonline.clientes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tiendaonline.clientes.dto.ClienteDto;
import com.tiendaonline.clientes.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
@RequestMapping("/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    //Inyeccion de dependencias
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
 
    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    public ClienteDto registrarCliente(@RequestBody @Valid ClienteDto cliente) {
        return clienteService.registrarCliente(cliente);
    }

    @GetMapping("/{id}")
    public ClienteDto consultarClientePorId(@PathVariable("id") Integer id) {
        return clienteService.consultarClientePorId(id);
    }

    //@GetMapping("/{id}/pedidos")
    //public ArrayList<PedidoDto> consultarPedidosCliente(@PathVariable Integer id) {
        
    //}
}
