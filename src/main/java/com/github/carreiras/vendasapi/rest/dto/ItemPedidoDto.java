package com.github.carreiras.vendasapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedidoDto {

    private Integer produto;
    private Integer quantidade;
}
