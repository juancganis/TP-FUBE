package com.tiendaonline.pedidos.repository;

import org.springframework.stereotype.Repository;
import com.tiendaonline.pedidos.models.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ItemsPedidoRepository extends JpaRepository<ItemPedido, Integer> {

}
