package com.tiendaonline.pedidos.controller;


import com.tiendaonline.pedidos.dto.PedidoRequestDto;
import com.tiendaonline.pedidos.dto.PedidoResponseDto;
import com.tiendaonline.pedidos.models.Pedido;
import com.tiendaonline.pedidos.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Esta anotacion serializa automáticamente los objetos retornados a JSON.
@RequestMapping("/v1/pedidos") // Definimos la url base para todos los endpoints de este recurso.
@Tag(name = "Pedidos", description = "API para la gestión del ciclo de vida de pedidos") // Esta anotacion agrupa todos los endpoints de este controlador bajo una misma sección en la interfaz de Swagger UI.
public class PedidoController {

    private final PedidoService pedidoService;

    // Inyección de dependencias mediante constructor:
    public PedidoController(PedidoService pedidoService){
        this.pedidoService = pedidoService;
    }

    // Metodo para crear un nuevo pedido.
    @PostMapping
    @Operation(summary = "Crear un pedido", description = "Valida el cliente, consulta precios, reserva stock y registra el pedido en estado CREADO.")
    public ResponseEntity<PedidoResponseDto> crearPedido(@Valid @RequestBody PedidoRequestDto pedidoRequestDto){
           // @Valid activa las validaciones declaradas en PedidoRequestDto y sus ítems antes de entrar al metodo.

        PedidoResponseDto pedidoNuevo = pedidoService.crearPedido(pedidoRequestDto);

        // Retornamos 201 (CREATED) junto con el recurso recién creado en el body.
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoNuevo);

    }

    // Metodo para consultar un pedido mediante su identificador (ID).
    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID", description = "Consulta (mediante su identificador) y devuelve el detalle completo de un pedido existente")
    public ResponseEntity<PedidoResponseDto> obtenerPedidoPorId(@PathVariable Integer id){

        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));

    }

    // Metodo para listar todos los pedidos de un cliente.
    @GetMapping
    @Operation(summary = "Lista todos los pedidos de un cliente", description = "Filtra todos los pedidos asociados a un clienteId específico.")
    public ResponseEntity<List<PedidoResponseDto>> listarPedidosPorCliente(@RequestParam Integer clienteId){

        return ResponseEntity.ok(pedidoService.listarPedidosPorCliente(clienteId));

    }

    // Metodo para cancelar un pedido existente con estado CREADO o CONFIRMADO, liberando la reserva de stock asociada en servicio-inventario.
    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar pedido en estado CREADO o CONFIRMADO", description = "Cambia el estado del pedido a CANCELADO y libera el stock reservado en servicio-inventario")
    public ResponseEntity<PedidoResponseDto> cancelarPedido(@PathVariable Integer idPedido){

        return ResponseEntity.ok((pedidoService.cancelarPedido(idPedido)));

    }

}
