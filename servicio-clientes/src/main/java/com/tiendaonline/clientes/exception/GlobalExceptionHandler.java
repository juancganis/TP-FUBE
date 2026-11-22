package com.tiendaonline.clientes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler (ClienteExistenteException.class)
    @ResponseStatus (HttpStatus.CONFLICT) //409
    public String manejarClienteExistente(ClienteExistenteException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler (ClienteNoExistentePorIdException.class)
    @ResponseStatus (HttpStatus.NOT_FOUND) //404
    public String manejarClienteNoExistentePorId(ClienteNoExistentePorIdException ex) {
        return ex.getMessage();
    }
}
