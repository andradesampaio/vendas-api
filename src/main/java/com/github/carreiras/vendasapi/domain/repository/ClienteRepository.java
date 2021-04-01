package com.github.carreiras.vendasapi.domain.repository;

import com.github.carreiras.vendasapi.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
