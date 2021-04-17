package com.github.carreiras.vendasapi.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredenciaisDto {

    private String login;
    private String senha;
}
