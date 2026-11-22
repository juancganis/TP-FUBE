package com.tiendaonline.pedidos.controller;


import com.tiendaonline.pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    // Inyección de dependencias mediante constructor:
    public PedidoController(PedidoService pedidoService){
        this.pedidoService = pedidoService;
    }




}
