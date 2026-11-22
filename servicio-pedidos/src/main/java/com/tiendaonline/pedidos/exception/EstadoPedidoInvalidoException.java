package com.tiendaonline.pedidos.exception;

// Se lanza al intentar realizar una operación no permitida para el estado actual del pedido, como cancelar uno ya cancelado o expirado.
public class EstadoPedidoInvalidoException extends RuntimeException {

    public EstadoPedidoInvalidoException(String mensaje){
        super(mensaje);
    }

}
