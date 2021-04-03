package com.github.carreiras.vendasapi.rest.controller;

import com.github.carreiras.vendasapi.domain.entity.ItemPedido;
import com.github.carreiras.vendasapi.domain.entity.Pedido;
import com.github.carreiras.vendasapi.domain.enums.StatusPedido;
import com.github.carreiras.vendasapi.rest.dto.AtualizacaoStatusPedidoDto;
import com.github.carreiras.vendasapi.rest.dto.InformacaoItemPedidoDto;
import com.github.carreiras.vendasapi.rest.dto.InformacaoPedidoDto;
import com.github.carreiras.vendasapi.rest.dto.PedidoDto;
import com.github.carreiras.vendasapi.service.PedidoService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer pedidoCreate(@RequestBody PedidoDto pedidoDto) {
        Pedido pedido = pedidoService.salvar(pedidoDto);
        return pedido.getId();
    }

    @GetMapping("{id}")
    public InformacaoPedidoDto pedidoById(@PathVariable Integer id) {
        return pedidoService.pedidoCompleto(id)
                .map(p -> converter(p))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Pedido não encontrado."));
    }

    private InformacaoPedidoDto converter(Pedido pedido) {
        return InformacaoPedidoDto
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .itens(converter(pedido.getItens()))
                .build();
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void pedidoStatusUpdate(@PathVariable Integer id,
                                   @RequestBody AtualizacaoStatusPedidoDto atualizacaoStatusPedidoDto) {
        String novoStatus = atualizacaoStatusPedidoDto.getNovoStatus();
        pedidoService.pedidoStatusUpdate(id, StatusPedido.valueOf(novoStatus));
    }

    private List<InformacaoItemPedidoDto> converter(List<ItemPedido> itens) {
        if (CollectionUtils.isEmpty(itens))
            return Collections.emptyList();

        return itens
                .stream()
                .map(item -> InformacaoItemPedidoDto
                        .builder()
                        .descricaoProduto(item.getProduto().getDescricao())
                        .precoUnitario(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
