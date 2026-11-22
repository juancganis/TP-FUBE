package com.tiendaonline.inventario.exception;


import com.tiendaonline.inventario.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // para cuando no se encuentra el producto
    @ResponseStatus (HttpStatus.NOT_FOUND) //404 no encontrado
    @ExceptionHandler (ProductoNoEncontrado.class)
    public ErrorResponseDto manejarProductoNoEncontrado(ProductoNoEncontrado ex) {

        return new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "Producto no encontrado",
                ex.getMessage()
        );
    }

    // para cuando no hay suficiente stock
    @ResponseStatus (HttpStatus.CONFLICT) //409 conflicto
    @ExceptionHandler (StockInsuficiente.class)
    public ErrorResponseDto manejarStockInsuficiente(StockInsuficiente ex){

        return new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                "Stock insuficiente",
                ex.getMessage()
        );
    }

    // para cuando no se encuentran reservas asociadas al pedido
    @ResponseStatus (HttpStatus.CONFLICT) //409 conflicto
    @ExceptionHandler (ReservasNoEncontradas.class)
    public ErrorResponseDto manejarReservasNoEncontradas(ReservasNoEncontradas ex){

        return new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                "Reservas no encontradas",
                ex.getMessage()
        );
    }

}
