package com.tiendaonline.clientes.dto;

import java.time.LocalDate;
//todo: agregar validaciones de campos a la hora de registrar el cliente
public record ClienteDto(
    String nombre,
    String email,
    LocalDate fechaAlta
) {}
