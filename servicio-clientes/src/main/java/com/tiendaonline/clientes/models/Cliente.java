package com.tiendaonline.clientes.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@NoArgsConstructor
@Entity 
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String nombre; // TODO implementar enmascaramiento (feature flag mask-pii)
    private String email; // TODO implementar enmascaramiento (feature flag mask-pii) 
    private LocalDate fechaAlta;

    public Cliente(String nombre, String email, LocalDate fechaAlta) {
        this.nombre = nombre;
        this.email = email;
        this.fechaAlta = fechaAlta;
    }
}
