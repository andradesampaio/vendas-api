package com.github.carreiras.vendasapi.rest;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    @Getter
    private List<String> errors;

    public ApiErrors(String error) {
        this.errors = Arrays.asList(error);
    }


}
