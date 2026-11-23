package com.tiendaonline.pedidos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import java.util.List;

@Builder
public record PedidoRequestDto(

     @NotNull(message = "El ID del cliente es obligatorio")               // Evitamos IDs nulos de clientes.
     @Positive(message = "El ID del cliente debe ser un número positivo") // Forzamos a que el ID del cliente  sea estrictamente mayor a 0.
     Integer clienteId,

     @NotEmpty(message = "El pedido debe contener al menos un ítem") // Nos asugramos de que la lista no sea null y tenga al menos un ítem.
     @Valid  // Extendemos la validación hacia cada ItemPedidoRequestDto interno de la lista.
     List<ItemPedidoRequestDto> listaItemsPedido

){}
