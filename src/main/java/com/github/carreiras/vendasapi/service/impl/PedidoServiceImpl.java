package com.github.carreiras.vendasapi.service.impl;

import com.github.carreiras.vendasapi.domain.entity.Cliente;
import com.github.carreiras.vendasapi.domain.entity.ItemPedido;
import com.github.carreiras.vendasapi.domain.entity.Pedido;
import com.github.carreiras.vendasapi.domain.entity.Produto;
import com.github.carreiras.vendasapi.domain.enums.StatusPedido;
import com.github.carreiras.vendasapi.domain.repository.ClienteRepository;
import com.github.carreiras.vendasapi.domain.repository.ItemPedidoRepository;
import com.github.carreiras.vendasapi.domain.repository.PedidoRepository;
import com.github.carreiras.vendasapi.domain.repository.ProdutoRepository;
import com.github.carreiras.vendasapi.exception.PedidoNaoEncontradoException;
import com.github.carreiras.vendasapi.exception.RegraNegocioException;
import com.github.carreiras.vendasapi.rest.dto.ItemPedidoDto;
import com.github.carreiras.vendasapi.rest.dto.PedidoDto;
import com.github.carreiras.vendasapi.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    public Pedido save(PedidoDto pedidoDto) {
        Integer idCliente = pedidoDto.getCliente();
        Cliente cliente = clienteRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(pedidoDto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itensPedido = itemPedidoConvert(pedido, pedidoDto.getItens());
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itensPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> bringComplete(Integer id) {
        return pedidoRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void updateStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepository.findById(id)
                .map(p -> {
                    p.setStatus(statusPedido);
                    return pedidoRepository.save(p);
                }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> itemPedidoConvert(Pedido pedido, List<ItemPedidoDto> itens) {
        if (itens.isEmpty()) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens.");
        }

        return itens
                .stream()
                .map(i -> {
                    Integer idProduto = i.getProduto();
                    Produto produto = produtoRepository
                            .findById(idProduto)
                            .orElseThrow(() -> new RegraNegocioException("Código de produto inválido."));
                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(i.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                })
                .collect(Collectors.toList());
    }
}
