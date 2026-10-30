package com.tiendaonline.inventario.controller;

import com.tiendaonline.inventario.service.ReservaService;

public class ReservaController {

    private ReservaService reservaService;

    public ReservaController(ReservaService reservaService){
        this.reservaService = reservaService;
    }

}
