package com.tiendaonline.pedidos.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.tiendaonline.pedidos.client.ClienteClient;
import com.tiendaonline.pedidos.client.InventarioClient;
import com.tiendaonline.pedidos.dto.*;
import com.tiendaonline.pedidos.enums.EstadoPedido;
import org.springframework.transaction.annotation.Transactional;
import com.tiendaonline.pedidos.exception.RecursoNoEncontradoException;
import com.tiendaonline.pedidos.exception.EstadoPedidoInvalidoException;
import com.tiendaonline.pedidos.mapper.PedidoMapper;
import com.tiendaonline.pedidos.models.ItemPedido;
import com.tiendaonline.pedidos.models.Pedido;
import com.tiendaonline.pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;


@Service
public class PedidoServiceImpl implements PedidoService{

    private final PedidoRepository pedidoRepository;
    private final ClienteClient clienteClient;
    private final InventarioClient inventarioClient;

    // Hacemos la inyección de dependencias por constructor:
    public PedidoServiceImpl(PedidoRepository pedidoRepository, ClienteClient clienteClient, InventarioClient inventarioClient) {
        this.pedidoRepository = pedidoRepository;
        this.clienteClient = clienteClient;
        this.inventarioClient = inventarioClient;
    }

    @Override
    @Transactional // Esto es para que, si falla la reserva de stock en inventario, revertimos automáticamente el guardado del pedido en la base de datos.
    public PedidoResponseDto crearPedido(PedidoRequestDto requestDto) {

        // Validamos que el cliente exista en servicio-clientes:
        clienteClient.validarExistenciaCliente(requestDto.clienteId());

        // Creamos una variable que almacene el tiempo actual de cuando se cree el pedido
        // (ya que, inicialmente, la fecha de actualizacion debe ser igual a la de creacion).
        LocalDateTime fechaAhora = LocalDateTime.now();

        // Transformamos el DTO en entidad y completamos sus datos iniciales:
        Pedido pedido = PedidoMapper.toEntity(requestDto);
        pedido.setEstado(EstadoPedido.CREADO);
        pedido.setFechaCreacion(fechaAhora);
        pedido.setFechaActualizacion(fechaAhora);

        BigDecimal montoTotal = BigDecimal.ZERO;

        // Consultamos cada producto en inventario por sku para obtener su id real y precio unitario actual:
        for (int i = 0; i < requestDto.listaItemsPedido().size(); i++) {

            ItemPedidoRequestDto itemDto = requestDto.listaItemsPedido().get(i);
            ItemPedido itemEntidad = pedido.getListaItemsPedido().get(i);

            ProductoResponseDto producto = inventarioClient.obtenerProducto(itemDto.sku());

            itemEntidad.setProductoId(producto.id());
            itemEntidad.setPrecioUnitario(producto.precioUnitario());

            BigDecimal subtotal = producto.precioUnitario().multiply(BigDecimal.valueOf(itemDto.cantidad()));
            montoTotal = montoTotal.add(subtotal);

        }

        pedido.setMontoTotal(montoTotal);

        // Persistimos el pedido para obtener su id generado antes de pedir las reservas:
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // Solicitamos la reserva de stock para cada ítem en servicio-inventario:
        for (ItemPedidoRequestDto itemDto : requestDto.listaItemsPedido()) {

            ReservaStockRequestDto reservaRequest = new ReservaStockRequestDto(
                    pedidoGuardado.getId(),
                    itemDto.cantidad(),
                    pedidoGuardado.getFechaCreacion(),
                    itemDto.sku()
            );
            inventarioClient.reservarStock(reservaRequest);

        }

        return PedidoMapper.toDTO(pedidoGuardado);

    }

    @Override
    @Transactional
    public PedidoResponseDto obtenerPedidoPorId(Integer pedidoId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el pedido con ID: " + pedidoId));

        return PedidoMapper.toDTO(pedido);

    }

    @Override
    @Transactional
    public List<PedidoResponseDto> listarPedidosPorCliente(Integer clienteId) {

        return pedidoRepository.findByClienteId(clienteId)
                .stream()
                .map(PedidoMapper::toDTO)
                .toList();

    }

    @Override
    @Transactional // Esto es para asegurarnos de que el cambio de estado a CANCELADO solo se confirme en la bdd si
                   // la liberación de stock en inventario fue exitosa.
    public PedidoResponseDto cancelarPedido(Integer pedidoId) {

        // Buscamos el pedido en la base de datos:
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el pedido con ID: " + pedidoId));

        // Invocamos al metodo auxiliar (resultado de la refactorizacion):
        validarEstadoCancelable(pedido);

        // Notificamos a servicio-inventario para liberar el stock reservado de este pedido:
        inventarioClient.liberarReservasPorPedido(pedidoId);

        // Actualizamos el estado a CANCELADO:
        pedido.setEstado(EstadoPedido.CANCELADO);

        // Le ponemos fecha de actualizacion "ahora":
        pedido.setFechaActualizacion(LocalDateTime.now());

        // Persistimos:
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        return PedidoMapper.toDTO(pedidoActualizado);

    }

    // Metodo auxiliar extraído del metodo "cancelarPedido", a modo de REFACTORIZACION (para separar la regla de validación del flujo principal).
    // Buscamos validar que el estado actual permita la cancelación (solo CREADO o CONFIRMADO):
    private void validarEstadoCancelable(Pedido pedido) {

        if (pedido.getEstado() == EstadoPedido.CANCELADO || pedido.getEstado() == EstadoPedido.EXPIRADO) {
            throw new EstadoPedidoInvalidoException("No se puede cancelar un pedido en estado: " + pedido.getEstado());
        }

    }

}