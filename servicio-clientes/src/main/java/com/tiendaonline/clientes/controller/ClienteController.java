package com.tiendaonline.clientes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tiendaonline.clientes.dto.ClienteDto;
import com.tiendaonline.clientes.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
@RequestMapping("/v1/clientes")
@Tag(name = "Clientes", description = "API para la gestión de clientes (creacion y consulta)")
public class ClienteController {

    private final ClienteService clienteService;

    //Inyeccion de dependencias
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
 
    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    @Operation(summary = "Registrar un cliente", description = "Recibe los datos de un nuevo cliente, valida el formato de los mismos y registra el cliente devolviendo un codigo 201 CREATED. En caso de que el cliente ya exista devuelve un 409 CONFLICT")
    public ClienteDto registrarCliente(@RequestBody @Valid ClienteDto cliente) {
        return clienteService.registrarCliente(cliente);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar los datos de un cliente", description = "Recibe el id de un cliente como path variable y, si el cliente existe, devuelve sus datos con un 200 OK (enmascarados o no dependiendo del feature flag mask-pii). Si el cliente no existe devuelve un 404 NOT_FOUND")
    public ClienteDto consultarClientePorId(@PathVariable("id") Integer id) {
        return clienteService.consultarClientePorId(id);
    }

    //@GetMapping("/{id}/pedidos")
    //public ArrayList<PedidoDto> consultarPedidosCliente(@PathVariable Integer id) {
        
    //}
}
