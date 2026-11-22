package com.tiendaonline.pedidos.exception;

import org.springframework.http.HttpStatus;
import com.tiendaonline.pedidos.dto.ErrorResponseDto;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Se lanza cuando no existe en el sistema el pedido solicitado o el cliente consultado.
    @ResponseStatus (HttpStatus.NOT_FOUND) // Código de estado: 404.
    @ExceptionHandler (RecursoNoEncontradoException.class)
    public ErrorResponseDto manejarRecursoNoEncontrado(RecursoNoEncontradoException ex){
        return new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage()
        );
    }

    // Se lanza cuando servicio-inventario rechaza la reserva porque no alcanza el stock disponible.
    @ResponseStatus (HttpStatus.CONFLICT) // Código de estado: 409.
    @ExceptionHandler (StockInsuficienteException.class)
    public ErrorResponseDto manejarStockInsuficiente(StockInsuficienteException ex){
        return new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                "Stock insuficiente",
                ex.getMessage()
        );
    }

    // Se lanza al intentar realizar una operación no permitida para el estado actual del pedido, como cancelar uno ya cancelado o expirado.
    @ResponseStatus (HttpStatus.CONFLICT) // Código de estado: 409.
    @ExceptionHandler (EstadoPedidoInvalidoException.class)
    public ErrorResponseDto manejarEstadoPedidoInvalido(EstadoPedidoInvalidoException ex){
        return new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                "Estado de pedido inválido",
                ex.getMessage()
        );
    }

    // Se lanza cuando falla la comunicación HTTP o se recibe un error inesperado desde servicio-clientes o servicio-inventario.
    @ResponseStatus(HttpStatus.BAD_GATEWAY) // Código de estado: 502.
    @ExceptionHandler(ServicioExternoException.class)
    public ErrorResponseDto manejarServicioExterno(ServicioExternoException ex) {
        return new ErrorResponseDto(
                HttpStatus.BAD_GATEWAY.value(),
                "Error de comunicación con servicio externo",
                ex.getMessage()
        );
    }

    // Captura errores de @Valid en los DTOs.
    @ResponseStatus (HttpStatus.BAD_REQUEST) // Código de estado: 400.
    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ErrorResponseDto manejarValidaciones(MethodArgumentNotValidException ex){
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                "Los datos enviados en la solicitud no son válidos",
                errores
        );
    }

    // Captura cualquier excepción no controlada para evitar exponer información interna al cliente.
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Código de estado: 500.
    @ExceptionHandler(Exception.class)
    public ErrorResponseDto manejarErrorGeneral(Exception ex) {
        return new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                "Ocurrió un error inesperado al procesar la solicitud"
        );
    }

}