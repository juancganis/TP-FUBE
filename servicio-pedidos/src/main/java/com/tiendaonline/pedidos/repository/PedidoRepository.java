package com.tiendaonline.pedidos.repository;

import com.tiendaonline.pedidos.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

}
