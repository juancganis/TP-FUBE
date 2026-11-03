package com.tiendaonline.clientes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    @ResponseStatus (HttpStatus.CONFLICT) //409
    @ExceptionHandler (ClienteExistenteException.class)
    public String manejarClienteExistente(ClienteExistenteException ex) {
        return ex.getMessage();
    }
}
