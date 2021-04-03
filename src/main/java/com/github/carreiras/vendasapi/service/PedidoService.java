package com.github.carreiras.vendasapi.service;

import com.github.carreiras.vendasapi.domain.entity.Pedido;
import com.github.carreiras.vendasapi.domain.enums.StatusPedido;
import com.github.carreiras.vendasapi.rest.dto.PedidoDto;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar(PedidoDto pedidoDto);

    Optional<Pedido> pedidoCompleto(Integer id);

    void pedidoStatusUpdate(Integer id, StatusPedido statusPedido);
}
