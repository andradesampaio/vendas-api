package com.github.carreiras.vendasapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {

    private String login;
    private String senha;
}
