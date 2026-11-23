package com.tiendaonline.clientes.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.tiendaonline.clientes.dto.PedidoDto;
import com.tiendaonline.clientes.exception.ServicioExternoException;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

@Component 
public class PedidosClient {
    // RestTemplate es el cliente HTTP de Spring que usamos para realizar peticiones REST hacia los otros microservicios
    // y convertir automáticamente los JSON de respuesta en objetos Java.
    private final RestTemplate restTemplate;
    private final String baseUrl;

    // Inyección de dependencias de RestTemplate.
    // @Value lee la propiedad 'servicios.pedidos.url' desde application.yml (para no hardcodear la URL).
    public PedidosClient(RestTemplate restTemplate, @Value("${servicios.pedidos.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<PedidoDto> consultarPedidosCliente(Integer id) {
        try {
            return restTemplate.exchange(
                    baseUrl + "/v1/pedidos?clienteId={id}",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PedidoDto>>() {},
                    id
            ).getBody();
        } catch (ResourceAccessException e) {
            throw new ServicioExternoException("El servicio de pedidos no está disponible en este momento");
        }
    }
}
