package com.github.carreiras.vendasapi.rest.controller;

import com.github.carreiras.vendasapi.domain.entity.Usuario;
import com.github.carreiras.vendasapi.exception.SenhaInvalidaException;
import com.github.carreiras.vendasapi.rest.dto.CredenciaisDto;
import com.github.carreiras.vendasapi.rest.dto.TokenDto;
import com.github.carreiras.vendasapi.security.jwt.JwtService;
import com.github.carreiras.vendasapi.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@Api("Api Usuários")
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("save - Salva um novo usuário")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Usuário salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
    })
    public Usuario save(@RequestBody @Valid Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioService.save(usuario);
    }

    @PostMapping("/auth")

    @ApiOperation("authenticate - Autentica um usuário existente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário autenticado com sucesso"),
            @ApiResponse(code = 401, message = "Usuário nao autorizado"),
    })
    public TokenDto authenticate(@RequestBody CredenciaisDto credenciais) {
        try {
            Usuario usuario = Usuario
                    .builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha())
                    .build();
            UserDetails usuarioAutenticado = usuarioService.authenticate(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDto(usuario.getLogin(), token);
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
