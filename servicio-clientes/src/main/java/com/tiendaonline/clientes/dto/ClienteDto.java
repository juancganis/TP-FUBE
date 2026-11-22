package com.tiendaonline.clientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public record ClienteDto(
    @NotBlank (message = "El nombre es obligatorio")
    String nombre,
    
    @NotBlank (message = "El email es obligatorio")
    @Email (message = "El email debe cumplir con el formato de correo")
    String email
) {}
