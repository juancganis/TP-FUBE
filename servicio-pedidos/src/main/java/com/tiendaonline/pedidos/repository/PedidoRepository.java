package com.tiendaonline.pedidos.repository;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import com.tiendaonline.pedidos.models.Pedido;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.tiendaonline.pedidos.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    // Con esta consulta obtenemos todos los pedidos asociados a un cliente específico:
    List<Pedido> findByClienteId(Integer clienteId);

    // Con esta consulta buscamos de forma paginada los pedidos en un estado determinado (ej. CREADO) cuya fecha de creación sea
    // anterior al límite indicado (para Spring Batch):
    Page<Pedido> findByEstadoAndFechaCreacionBefore(EstadoPedido estado, LocalDateTime fechaLimite, Pageable pageable);

}
