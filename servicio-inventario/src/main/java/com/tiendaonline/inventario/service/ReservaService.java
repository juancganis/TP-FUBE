package com.tiendaonline.inventario.service;

import com.tiendaonline.inventario.dto.ReservaResponseDto;

public interface ReservaService {
    ReservaResponseDto crearReserva(ReservaResponseDto reservaResponseDto);
    void liberarReserva(Integer idPedido);
}
