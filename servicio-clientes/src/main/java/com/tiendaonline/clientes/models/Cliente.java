package com.tiendaonline.clientes.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

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
    private String nombre;
    private String email;
    private LocalDateTime fechaAlta;

    public Cliente(String nombre, String email, LocalDateTime fechaAlta) {
        this.nombre = nombre;
        this.email = email;
        this.fechaAlta = fechaAlta;
    }

    public String nombreEnmascarado() {
        if (this.nombre == null || this.nombre.isBlank()) {
            return this.nombre;
        }
        return Arrays.stream(this.nombre.trim().split("\\s+"))
                .map(Cliente::enmascararPalabra)
                .collect(Collectors.joining(" "));
    }

    public String emailEnmascarado() {
        if (this.email == null || this.email.isBlank()) {
            return this.email;
        }
        int arroba = this.email.indexOf('@');
        int ultimoPunto = this.email.lastIndexOf('.');
        if (arroba <= 0 || ultimoPunto < arroba) {
            return "****"; // formato inesperado: no se filtra nada
        }
        String extension = this.email.substring(ultimoPunto); // incluye el punto
        return "****@****" + extension;
    }

    private static String enmascararPalabra(String palabra) {
        return palabra.charAt(0) + "***";
    }
}
