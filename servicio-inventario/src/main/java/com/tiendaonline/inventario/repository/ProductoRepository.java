package com.tiendaonline.inventario.repository;

import com.tiendaonline.inventario.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
