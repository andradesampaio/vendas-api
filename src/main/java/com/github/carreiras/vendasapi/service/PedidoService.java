package com.github.carreiras.vendasapi.service;

import com.github.carreiras.vendasapi.domain.entity.Pedido;
import com.github.carreiras.vendasapi.domain.enums.StatusPedido;
import com.github.carreiras.vendasapi.rest.dto.PedidoDto;

import java.util.Optional;

public interface PedidoService {

    Pedido save(PedidoDto pedidoDto);

    Optional<Pedido> bringComplete(Integer id);

    void updateStatus(Integer id, StatusPedido statusPedido);
}
