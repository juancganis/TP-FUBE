package com.tiendaonline.clientes.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
//todo: agregar validaciones de campos a la hora de registrar el cliente
public record ClienteDto(
    @NotBlank 
    String nombre,
    
    @NotBlank @Email 
    String email
) {}
