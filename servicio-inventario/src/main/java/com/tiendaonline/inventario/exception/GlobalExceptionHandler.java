package com.tiendaonline.inventario.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus (HttpStatus.NOT_FOUND) //404 no encontrado
    @ExceptionHandler (ProductoNoEncontrado.class)
    public String manejarProductoNoEncontrado(ProductoNoEncontrado ex) {
        return ex.getMessage();
    }

    @ResponseStatus (HttpStatus.CONFLICT) //409 conflicto
    @ExceptionHandler (StockInsuficiente.class)
    public String manejarStockInsuficiente(StockInsuficiente ex){
        return ex.getMessage();
    }

    @ResponseStatus (HttpStatus.CONFLICT) //409 conflicto
    @ExceptionHandler (ReservasNoEncontradas.class)
    public String manejarReservasNoEncontradas(ReservasNoEncontradas ex){
        return ex.getMessage();
    }

}
