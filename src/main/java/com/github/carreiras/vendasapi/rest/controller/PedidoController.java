package com.github.carreiras.vendasapi.rest.controller;

import com.github.carreiras.vendasapi.domain.entity.ItemPedido;
import com.github.carreiras.vendasapi.domain.entity.Pedido;
import com.github.carreiras.vendasapi.domain.enums.StatusPedido;
import com.github.carreiras.vendasapi.rest.dto.AtualizacaoStatusPedidoDto;
import com.github.carreiras.vendasapi.rest.dto.InformacaoItemPedidoDto;
import com.github.carreiras.vendasapi.rest.dto.InformacaoPedidoDto;
import com.github.carreiras.vendasapi.rest.dto.PedidoDto;
import com.github.carreiras.vendasapi.service.PedidoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@Api("Api Pedidos")
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("save - Salva um novo pedido")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pedido salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
    })
    public Integer save(@RequestBody @Valid PedidoDto pedidoDto) {
        Pedido pedido = pedidoService.save(pedidoDto);
        return pedido.getId();
    }

    @GetMapping("{id}")
    @ApiOperation("bringComplete - Retorna um pedido completo")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pedido encontrado"),
            @ApiResponse(code = 404, message = "Pedido não encontrado"),
    })
    public InformacaoPedidoDto bringComplete(@PathVariable Integer id) {
        return pedidoService.bringComplete(id)
                .map(p -> toConvert(p))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Pedido não encontrado."));
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("updateStatus - Atualiza Status de pedido")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Status de pedido atualizado"),
            @ApiResponse(code = 404, message = "Pedido não encontrado"),
    })
    public void updateStatus(@PathVariable Integer id,
                             @RequestBody AtualizacaoStatusPedidoDto atualizacaoStatusPedidoDto) {
        String novoStatus = atualizacaoStatusPedidoDto.getNovoStatus();
        pedidoService.updateStatus(id, StatusPedido.valueOf(novoStatus));
    }

    private List<InformacaoItemPedidoDto> toConvert(List<ItemPedido> itens) {
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

    private InformacaoPedidoDto toConvert(Pedido pedido) {
        return InformacaoPedidoDto
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .itens(toConvert(pedido.getItens()))
                .build();
    }
}
