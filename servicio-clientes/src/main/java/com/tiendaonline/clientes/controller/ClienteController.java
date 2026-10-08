package com.tiendaonline.clientes.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendaonline.clientes.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
     
    private ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
}
