package com.tiendaonline.clientes.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Cliente {

    private Integer id; // todo: FALTAN ANOTACIONES DE AUTOINCREMENTAL.
    private String nombre; // todo: FALTA PII.
    private String email; // todo: FALTA PII.
    private LocalDate fechaAlta;

    public Cliente(String nombre, String email, LocalDate fechaAlta) {
        this.nombre = nombre;
        this.email = email;
        this.fechaAlta = fechaAlta;
    }
}
