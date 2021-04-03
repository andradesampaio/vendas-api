package com.github.carreiras.vendasapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {

    private Integer cliente;
    private BigDecimal total;
    private List<ItemPedidoDto> itens;
}
