package com.tiendaonline.clientes.exception;

public class ClienteExistenteException extends RuntimeException{
    public ClienteExistenteException(String mensaje) {
        super(mensaje);
    }
}
