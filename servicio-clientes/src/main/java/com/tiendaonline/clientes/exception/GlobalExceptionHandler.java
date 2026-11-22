package com.tiendaonline.clientes.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tiendaonline.clientes.dto.ErrorResponseDto;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler (ClienteExistenteException.class)
    @ResponseStatus (HttpStatus.CONFLICT) //409
    public ErrorResponseDto manejarClienteExistente(ClienteExistenteException ex) {
        return new ErrorResponseDto(
            HttpStatus.CONFLICT.value(),
            "Cliente Existente",
            ex.getMessage());
    }

    @ExceptionHandler (ClienteNoExistentePorIdException.class)
    @ResponseStatus (HttpStatus.NOT_FOUND) //404
    public ErrorResponseDto manejarClienteNoExistentePorId(ClienteNoExistentePorIdException ex) {
        return new ErrorResponseDto(
            HttpStatus.NOT_FOUND.value(),
            "Cliente No Existente",
            ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errores.putIfAbsent(error.getField(), error.getDefaultMessage())
        );
        return errores;
    }
}
