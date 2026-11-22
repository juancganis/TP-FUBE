package com.tiendaonline.clientes.exception;

public class ClienteNoExistentePorIdException extends RuntimeException{
    public ClienteNoExistentePorIdException(String mensaje) {
        super(mensaje);
    }
}
