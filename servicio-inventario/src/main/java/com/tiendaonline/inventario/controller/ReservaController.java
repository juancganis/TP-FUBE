package com.tiendaonline.inventario.controller;

import com.tiendaonline.inventario.dto.ReservaResponseDto;
import com.tiendaonline.inventario.service.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/V1/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    // inyeccion del constructor
    public ReservaController(ReservaService reservaService){
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDto> crearReserva(@RequestBody ReservaResponseDto reservaResponseDto){
        ReservaResponseDto reservaCreada = reservaService.crearReserva(reservaResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaCreada);
    }

    @PostMapping("/liberar/{pedidoId}")
    public ResponseEntity<Void> liberarReserva(@PathVariable Integer pedidoId){
        reservaService.liberarReserva(pedidoId);
        return ResponseEntity.noContent().build();
    }
}
