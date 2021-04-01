package com.github.carreiras.vendasapi.domain.repository;

import com.github.carreiras.vendasapi.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}
