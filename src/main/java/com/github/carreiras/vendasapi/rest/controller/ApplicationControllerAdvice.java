package com.github.carreiras.vendasapi.rest.controller;

import com.github.carreiras.vendasapi.exception.PedidoNaoEncontradoException;
import com.github.carreiras.vendasapi.exception.RegraNegocioException;
import com.github.carreiras.vendasapi.rest.ApiErrors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(RegraNegocioException.class)
    public ApiErrors handleRegraNegociosException(RegraNegocioException ex) {
        String message = ex.getMessage();
        return new ApiErrors(message);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ApiErrors handlePedidoNaoEncontradoException(PedidoNaoEncontradoException ex) {
        return new ApiErrors(ex.getMessage());
    }
}
