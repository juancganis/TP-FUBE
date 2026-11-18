package com.tiendaonline.inventario.service;

import com.tiendaonline.inventario.dto.ReservaDto;

public interface ReservaService {
    ReservaDto crearReserva(ReservaDto reservaDto);
    void liberarReserva(Integer idPedido); //todo ver que le tengo que pasar para liberarla
}
