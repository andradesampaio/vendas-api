package com.github.carreiras.vendasapi.rest.controller;

import com.github.carreiras.vendasapi.domain.entity.Produto;
import com.github.carreiras.vendasapi.domain.repository.ProdutoRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/produtos")
public class ProdutoController {

    private static final String NAO_ENCONTRADO = "Produto não encontrado.";

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public Produto produtoCreate(@RequestBody @Valid Produto produto) {
        return produtoRepository.save(produto);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void produtoUpdate(@PathVariable Integer id,
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
    public void produtoDelete(@PathVariable Integer id) {
        produtoRepository
                .findById(id)
                .map(p -> {
                    produtoRepository.delete(p);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NAO_ENCONTRADO));
    }

    @GetMapping()
    public List<Produto> produtoFilter(Produto produto) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example produtoFilter = Example.of(produto, matcher);
        return produtoRepository.findAll(produtoFilter);
    }

    @GetMapping("/{id}")
    public Produto produtoById(@PathVariable Integer id) {
        return produtoRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NAO_ENCONTRADO));
    }
}
