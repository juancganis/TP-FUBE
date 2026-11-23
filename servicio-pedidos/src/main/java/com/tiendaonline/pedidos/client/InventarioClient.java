package com.tiendaonline.pedidos.client;

import java.math.BigDecimal;

import com.tiendaonline.pedidos.dto.ProductoResponseDto;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.beans.factory.annotation.Value;
import com.tiendaonline.pedidos.dto.ReservaStockRequestDto;
import com.tiendaonline.pedidos.exception.ServicioExternoException;
import com.tiendaonline.pedidos.exception.StockInsuficienteException;
import com.tiendaonline.pedidos.exception.RecursoNoEncontradoException;
import org.springframework.web.client.RestTemplate;

// Esta anotación registra esta clase como un Bean administrado por el Application Context de Spring,
// permitiendo inyectarla automáticamente en PedidoService.
@Component
public class InventarioClient {

    // RestTemplate es el cliente HTTP de Spring que usamos para realizar peticiones REST hacia los otros microservicios
    // y convertir automáticamente los JSON de respuesta en objetos Java.
    private final RestTemplate restTemplate;
    private final String baseUrl;

    // Inyección de dependencias de RestTemplate.
    // @Value lee la propiedad 'servicios.inventario.url' desde application.yml (para no hardcodear la URL).
    public InventarioClient(RestTemplate restTemplate, @Value("${servicios.inventario.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    // 1. Con el siguiente metodo, enviamos un GET a servicio-inventario para consulta el producto (y obtener su precioUnitario y su sku).
    public ProductoResponseDto obtenerProducto(String sku) {
        try {
            return restTemplate.getForObject(baseUrl + "/v1/productos/" + sku + "/stock", ProductoResponseDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RecursoNoEncontradoException("No existe el producto con ID: " + sku);
        }
    }

    // 2. Con el siguiente metodo, enviamos un POST a servicio-inventario para reservar stock para un pedido;
    // si no alcanza, inventario devuelve 400/409 y lanzamos StockInsuficienteException.
    public void reservarStock(ReservaStockRequestDto request) {

        // Si bien servicio-inventario valida el stock y responde con un error HTTP (400/409),
        // RestTemplate lanza HttpClientErrorException al recibir un 4xx; la capturamos para
        // traducirla a una excepción de dominio propia y evitar que escale como un error 500.
        try {
            restTemplate.postForObject(baseUrl + "/v1/reservas", request, Object.class);
        } catch (HttpClientErrorException e) {
            throw new StockInsuficienteException("Stock insuficiente para el producto con SKU: " + request.sku());
        }
    }

    // 3. Con el siguiente metodo, enviamos un POST a servicio-inventario para cambiar el estado de las reservas de ese pedido a LIBERADA.
    // Se pasa 'null' como segundo argumento porque el pedidoId viaja en la URL y no se envía body en la petición.
    public void liberarReservasPorPedido(Integer pedidoId) {
        restTemplate.postForObject(baseUrl + "/v1/reservas/liberar/" + pedidoId, null, Void.class);
    }

}