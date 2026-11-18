package com.tiendaonline.inventario.service;

import com.tiendaonline.inventario.dto.ReservaDto;
import com.tiendaonline.inventario.models.Reserva;

public interface ReservaService {
    ReservaDto crearReserva(Reserva reserva);
    void liberarReserva(); //todo ver que le tengo que pasar para liberarla
}
