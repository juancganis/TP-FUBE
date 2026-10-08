package com.tiendaonline.clientes.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    private Integer id; // todo: FALTAN ANOTACIONES DE AUTOINCREMENTAL.
    private String nombre; // todo: FALTA PII.
    private String email; // todo: FALTA PII.
    private LocalDate fechaAlta;


}
