package com.tiendaonline.inventario.controller;

import com.tiendaonline.inventario.dto.ReservaResponseDto;
import com.tiendaonline.inventario.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/V1/reserva")
public class ReservaController {

    private final ReservaService reservaService;

    // inyeccion del constructor
    public ReservaController(ReservaService reservaService){
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDto> crearReserva(@RequestBody ReservaResponseDto reservaDto){
        ReservaResponseDto reservaCreada = reservaService.crearReserva(reservaDto);
        return ResponseEntity.ok(reservaCreada);
    }

}
