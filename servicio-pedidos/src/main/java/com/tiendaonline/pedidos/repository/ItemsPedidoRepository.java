package com.tiendaonline.pedidos.repository;

import com.tiendaonline.pedidos.models.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsPedidoRepository extends JpaRepository<ItemPedido, Integer> {
}
