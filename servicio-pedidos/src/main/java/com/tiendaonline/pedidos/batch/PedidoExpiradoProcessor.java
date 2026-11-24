package com.tiendaonline.pedidos.batch;

import com.tiendaonline.pedidos.client.InventarioClient;
import com.tiendaonline.pedidos.enums.EstadoPedido;
import com.tiendaonline.pedidos.models.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
// Implementamos ItemProcessor<I, O>, el cual recibe cada entidad Pedido
// leída por el Reader (I) y devuelve el Pedido modificado hacia el Writer (O).
public class PedidoExpiradoProcessor implements ItemProcessor<Pedido, Pedido> {

    private final InventarioClient inventarioClient;

    // Inyección de dependencias:
    public PedidoExpiradoProcessor(InventarioClient inventarioClient) {
        this.inventarioClient = inventarioClient;
    }

    @Override
    public Pedido process(Pedido pedido) {

        // Creamos un log para indicar que se va a expirar un pedido vencido (mostrando id y fecha de creación del pedido).
        log.info("Expirando pedido VENCIDO con ID = {} (creado en la fecha {})."
                , pedido.getId(), pedido.getFechaCreacion());

        // Liberamos el stock previamente reservado en servicio-inventario:
        inventarioClient.liberarReservasPorPedido(pedido.getId());

        // Actualizamos el estado y la fecha de actualización antes de enviarlo al ItemWriter:
        pedido.setEstado(EstadoPedido.EXPIRADO);
        pedido.setFechaActualizacion(LocalDateTime.now());

        return pedido;
    }

}
