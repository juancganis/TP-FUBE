package com.tiendaonline.inventario.repository;

import com.tiendaonline.inventario.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByIdPedido(Integer IdPedido);
}
