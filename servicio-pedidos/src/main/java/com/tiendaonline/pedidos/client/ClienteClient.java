package com.tiendaonline.pedidos.client;

import com.tiendaonline.pedidos.exception.ServicioExternoException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import com.tiendaonline.pedidos.exception.RecursoNoEncontradoException;

// Esta anotación registra esta clase como un Bean administrado por el Application Context de Spring,
// permitiendo inyectarla automáticamente en PedidoService.
@Component
public class ClienteClient {

    // RestTemplate es el cliente HTTP de Spring que usamos para realizar peticiones REST hacia los otros microservicios
    // y convertir automáticamente los JSON de respuesta en objetos Java.
    private final RestTemplate restTemplate;
    private final String baseUrl;

    // Inyección de dependencias de RestTemplate.
    // @Value lee la propiedad 'servicios.clientes.url' desde application.yml (para no hardcodear la URL).
    public ClienteClient(RestTemplate restTemplate, @Value("${servicios.clientes.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    // Con el siguiente metodo envíamos un GET a servicio-clientes para consultar y  validar de forma síncrona que el cliente exista:
    public void validarExistenciaCliente(Integer clienteId) {
        try {

            restTemplate.getForObject(baseUrl + "/v1/clientes/" + clienteId, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RecursoNoEncontradoException("No existe el cliente con ID: " + clienteId);
        } catch (ResourceAccessException e) {
            // Se dispara cuando el microservicio destino está apagado o rechazó la conexión (o se terminó el tiempo).
            throw new ServicioExternoException("El servicio de clientes no está disponible en este momento");
        }
    }

}