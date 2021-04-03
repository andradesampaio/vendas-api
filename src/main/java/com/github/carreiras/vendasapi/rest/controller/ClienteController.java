package com.github.carreiras.vendasapi.rest.controller;

import com.github.carreiras.vendasapi.domain.entity.Cliente;
import com.github.carreiras.vendasapi.domain.repository.ClienteRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/clientes")
public class ClienteController {

    private static final String NAO_ENCONTRADO = "Cliente não encontrado.";

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public Cliente clienteCreate(@RequestBody @Valid Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void clienteUpdate(@PathVariable Integer id,
                              @RequestBody @Valid Cliente cliente) {
        clienteRepository
                .findById(id)
                .map(c -> {
                    cliente.setId(c.getId());
                    clienteRepository.save(cliente);
                    return c;
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NAO_ENCONTRADO));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void clienteDelete(@PathVariable Integer id) {
        clienteRepository
                .findById(id)
                .map(c -> {
                    clienteRepository.delete(c);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NAO_ENCONTRADO));
    }

    @GetMapping()
    public List<Cliente> clienteFilter(Cliente cliente) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example clienteFilter = Example.of(cliente, matcher);
        return clienteRepository.findAll(clienteFilter);
    }

    @GetMapping("/{id}")
    public Cliente clienteById(@PathVariable Integer id) {
        return clienteRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NAO_ENCONTRADO));
    }
}
