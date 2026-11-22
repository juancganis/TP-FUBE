package com.tiendaonline.inventario.exception;

public class StockInsuficiente extends RuntimeException {
    public StockInsuficiente(String message) {
        super(message);
    }
}
