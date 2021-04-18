package com.github.carreiras.vendasapi.rest.controller;

import com.github.carreiras.vendasapi.domain.entity.Produto;
import com.github.carreiras.vendasapi.domain.repository.ProdutoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@Api("Api Produtos")
@RequestMapping("api/produtos")
public class ProdutoController {

    private static final String NAO_ENCONTRADO = "Produto não encontrado.";

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    @ApiOperation("save - Salva um novo produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Produto salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
    })
    public Produto save(@RequestBody @Valid Produto produto) {
        return produtoRepository.save(produto);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("update - Atualizar um produto existente")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Produto atualizado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
    })
    public void update(@PathVariable Integer id,
                       @RequestBody @Valid Produto produto) {
        produtoRepository
                .findById(id)
                .map(p -> {
                    produto.setId(p.getId());
                    produtoRepository.save(produto);
                    return p;
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NAO_ENCONTRADO));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("delete - Exclui um produto existente")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Produto excluido com sucesso")
    })
    public void delete(@PathVariable Integer id) {
        produtoRepository
                .findById(id)
                .map(p -> {
                    produtoRepository.delete(p);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NAO_ENCONTRADO));
    }

    @GetMapping()
    @ApiOperation("filter - Filtra produto")
    @ApiResponse(code = 200, message = "Filtro executado")
    public List<Produto> filter(Produto produto) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example filter = Example.of(produto, matcher);
        return produtoRepository.findAll(filter);
    }

    @GetMapping("/{id}")
    @ApiOperation("findById - Obtém detalhes de um produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto encontrado"),
            @ApiResponse(code = 404, message = "Produto não encontrado"),
    })
    public Produto findById(@PathVariable Integer id) {
        return produtoRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NAO_ENCONTRADO));
    }
}
