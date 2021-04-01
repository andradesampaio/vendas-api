package com.github.carreiras.vendasapi.domain.repository;

import com.github.carreiras.vendasapi.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}
