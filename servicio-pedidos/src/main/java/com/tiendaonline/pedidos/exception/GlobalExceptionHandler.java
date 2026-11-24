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

        return ErrorResponseDto.builder()
                .estado(HttpStatus.NOT_FOUND.value())
                .error("Recurso no encontrado")
                .mensaje(ex.getMessage())
                .build();

    }

    // Se lanza cuando servicio-inventario rechaza la reserva porque no alcanza el stock disponible.
    @ResponseStatus (HttpStatus.CONFLICT) // Código de estado: 409.
    @ExceptionHandler (StockInsuficienteException.class)
    public ErrorResponseDto manejarStockInsuficiente(StockInsuficienteException ex){

        return ErrorResponseDto.builder()
                .estado(HttpStatus.CONFLICT.value())
                .error("Stock insuficiente")
                .mensaje(ex.getMessage())
                .build();

    }

    // Se lanza al intentar realizar una operación no permitida para el estado actual del pedido, como cancelar uno ya cancelado o expirado.
    @ResponseStatus (HttpStatus.CONFLICT) // Código de estado: 409.
    @ExceptionHandler (EstadoPedidoInvalidoException.class)
    public ErrorResponseDto manejarEstadoPedidoInvalido(EstadoPedidoInvalidoException ex){

        return ErrorResponseDto.builder()
                .estado(HttpStatus.CONFLICT.value())
                .error("Estado de pedido inválido")
                .mensaje(ex.getMessage())
                .build();

    }

    // Se lanza cuando falla la comunicación HTTP o se recibe un error inesperado desde servicio-clientes o servicio-inventario.
    @ResponseStatus(HttpStatus.BAD_GATEWAY) // Código de estado: 502.
    @ExceptionHandler(ServicioExternoException.class)
    public ErrorResponseDto manejarServicioExterno(ServicioExternoException ex) {

        return ErrorResponseDto.builder()
                .estado(HttpStatus.BAD_GATEWAY.value())
                .error("Error de comunicación con servicio externo")
                .mensaje(ex.getMessage())
                .build();

    }

    // Nos sirve para capturar errores de @Valid en los DTOs.
    @ResponseStatus (HttpStatus.BAD_REQUEST) // Código de estado: 400.
    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ErrorResponseDto manejarValidaciones(MethodArgumentNotValidException ex){

        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return ErrorResponseDto.builder()
                .estado(HttpStatus.BAD_REQUEST.value())
                .error("Error de validación")
                .mensaje("Los datos enviados en la solicitud no son válidos")
                .build();

    }

    // Nos sirve para capturar cualquier excepción no controlada y evitar exponer información interna al cliente.
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Código de estado: 500.
    @ExceptionHandler(Exception.class)
    public ErrorResponseDto manejarErrorGeneral(Exception ex) {

        return ErrorResponseDto.builder()
                .estado(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Error interno del servidor")
                .mensaje("Ocurrió un error inesperado al procesar la solicitud")
                .build();

    }

}