package com.github.carreiras.vendasapi.rest.dto;

import com.github.carreiras.vendasapi.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {

    @NotNull(message = "Informe o código do cliente.")
    private Integer cliente;

    @NotNull(message = "Campo TOTAL do pedido é obrigatório.")
    private BigDecimal total;

    @NotEmptyList(message = "Pedido nao pode ser realizado sem itens.")
    private List<ItemPedidoDto> itens;
}
