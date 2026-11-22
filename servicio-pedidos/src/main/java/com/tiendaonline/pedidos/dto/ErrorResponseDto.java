package com.tiendaonline.pedidos.dto;

import java.util.List;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;

// Esta clase existe a fin de usar un record inmutable en lugar de un String plano para estandarizar todas las respuestas de error en un
// formato JSON estructurado y conocido (con código, descripción, mensaje y detalles), facilitando su lectura por otros microservicios y clientes.

// Esta anotación le indica a Jackson que omita del JSON generado cualquier propiedad cuyo valor sea null al serializar un objeto Java:
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDto(
        LocalDateTime fechaYHora,
        int estado,
        String error,
        String mensaje,
        List<String> detalles
) {
    // Constructor para errores simples (sin lista de detalles):
    public ErrorResponseDto(int estado, String error, String mensaje) {
        this(LocalDateTime.now(), estado, error, mensaje, null);
    }

    // Constructor para errores múltiples (ej. validaciones de campos):
    public ErrorResponseDto(int estado, String error, String mensaje, List<String> detalles) {
        this(LocalDateTime.now(), estado, error, mensaje, detalles);
    }
}
