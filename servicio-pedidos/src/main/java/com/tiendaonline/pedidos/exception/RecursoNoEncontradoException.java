package com.tiendaonline.pedidos.exception;

// Se lanza cuando no existe en el sistema el pedido solicitado o el cliente consultado.
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje){
        super(mensaje);
    }

}
