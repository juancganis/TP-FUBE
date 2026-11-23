package com.tiendaonline.pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ItemPedidoRequestDto(

     @NotBlank(message = "El SKU del producto es obligatorio") // Validamos que el SKU no sea null, ni vacío "", ni solo espacios en blanco "   ".
     String sku,

     @NotNull(message = "La cantidad es obligatoria")         // Evitamos cantidades nulas.
     @Positive(message = "La cantidad debe ser mayor a cero") // Forzamos a que la cantidad sea estrictamente mayor a 0 (1 o más).
     Integer cantidad

) {}
