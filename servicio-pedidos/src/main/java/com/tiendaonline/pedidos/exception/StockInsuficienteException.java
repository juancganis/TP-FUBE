package com.tiendaonline.pedidos.exception;

// Se lanza cuando servicio-inventario rechaza la reserva porque no alcanza el stock disponible.
public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String mensaje){
        super(mensaje);
    }

}
