package com.tiendaonline.clientes.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendaonline.clientes.dto.ClienteDto;
import com.tiendaonline.clientes.models.Cliente;
import com.tiendaonline.clientes.service.ClienteService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/clientes")
public class ClienteController {
     
    private final ClienteService clienteService;

    //Inyeccion de dependencias
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    //@PostMapping
    //public ClienteDto registrarCliente(@RequestBody @Valid ClienteDto cliente) {

    //}

    @GetMapping("/{id}")
    public ClienteDto consultarClientePorId(@PathVariable Integer id) {
        return clienteService.consultarClientePorId(id);
    }

    //@GetMapping("/{id}/pedidos")
    //public ArrayList<Pedidos> consultarPedidosCliente(@PathVariable Integer id) {
        
    //}
}
