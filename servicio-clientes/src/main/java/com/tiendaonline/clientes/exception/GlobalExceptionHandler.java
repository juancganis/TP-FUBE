package com.tiendaonline.clientes.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tiendaonline.clientes.dto.ErrorResponseDto;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice 
@Slf4j 
public class GlobalExceptionHandler {
    
    @ExceptionHandler (ClienteExistenteException.class)
    @ResponseStatus (HttpStatus.CONFLICT) //409
    public ErrorResponseDto manejarClienteExistente(ClienteExistenteException ex) {
        log.warn("409 Conflict: {}", ex.getMessage());
        return new ErrorResponseDto(
            HttpStatus.CONFLICT.value(),
            "Cliente Existente",
            ex.getMessage());
    }

    @ExceptionHandler (ClienteNoExistentePorIdException.class)
    @ResponseStatus (HttpStatus.NOT_FOUND) //404
    public ErrorResponseDto manejarClienteNoExistentePorId(ClienteNoExistentePorIdException ex) {
        log.warn("404 Not Found: {}", ex.getMessage());
        return new ErrorResponseDto(
            HttpStatus.NOT_FOUND.value(),
            "Cliente No Existente",
            ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public Map<String, String> handleValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errores.putIfAbsent(error.getField(), error.getDefaultMessage())
        );
        log.warn("400 Bad Request: fallo la validacion en los campos {}", errores.keySet());
        return errores;
    }

    @ExceptionHandler(ServicioExternoException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) //503
    public ErrorResponseDto manejarServicioExterno(ServicioExternoException ex) {
        log.error("503 Service Unavailable: {}", ex.getMessage());
        return new ErrorResponseDto(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            "Servicio Externo No Disponible",
            ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorResponseDto manejarErrorInesperado(Exception ex) {
        log.error("500 Error inesperado", ex);
        return new ErrorResponseDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error Interno",
            "Ocurrió un error inesperado");
    }
}
