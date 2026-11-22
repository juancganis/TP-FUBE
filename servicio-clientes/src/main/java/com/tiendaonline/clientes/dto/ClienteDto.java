package com.tiendaonline.clientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public record ClienteDto(
    @NotBlank 
    String nombre,
    
    @NotBlank @Email 
    String email
) {}
