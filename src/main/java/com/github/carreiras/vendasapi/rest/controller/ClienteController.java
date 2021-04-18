package com.github.carreiras.vendasapi.rest.controller;

import com.github.carreiras.vendasapi.domain.entity.Cliente;
import com.github.carreiras.vendasapi.domain.repository.ClienteRepository;
import io.swagger.annotations.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@Api("Api Clientes")
@RequestMapping("api/clientes")
public class ClienteController {

    private static final String NAO_ENCONTRADO = "Cliente não encontrado.";

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    @ApiOperation("save - Salva um novo cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
    })
    public Cliente save(@RequestBody @Valid Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("update - Atualizar um cliente existente")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cliente atualizado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
    })
    public void update(@PathVariable Integer id,
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
    @ApiOperation("delete - Exclui um cliente existente")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cliente excluido com sucesso")
    })
    public void delete(@PathVariable Integer id) {
        clienteRepository
                .findById(id)
                .map(c -> {
                    clienteRepository.delete(c);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NAO_ENCONTRADO));
    }

    @GetMapping()
    @ApiOperation("filter - Filtra clientes")
    @ApiResponse(code = 200, message = "Filtro executado")
    public List<Cliente> filter(Cliente cliente) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example filter = Example.of(cliente, matcher);
        return clienteRepository.findAll(filter);
    }

    @GetMapping("/{id}")
    @ApiOperation("findById - Obtém detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado"),
            @ApiResponse(code = 404, message = "Cliente não encontrado"),
    })
    public Cliente findById(@PathVariable @ApiParam("Id do cliente") Integer id) {
        return clienteRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NAO_ENCONTRADO));
    }
}
