package com.tiendaonline.clientes.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table (name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre; // TODO implementar enmascaramiento (feature flag mask-pii)
    private String email; // TODO implementar enmascaramiento (feature flag mask-pii) 
    private LocalDateTime fechaAlta;

    public Cliente(String nombre, String email, LocalDateTime fechaAlta) {
        this.nombre = nombre;
        this.email = email;
        this.fechaAlta = fechaAlta;
    }
}
