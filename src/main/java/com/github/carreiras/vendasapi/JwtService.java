package com.github.carreiras.vendasapi;

import com.github.carreiras.vendasapi.domain.entity.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.expiracao}")
    private String expiracao;

    @Value("${security.jwt.chaveAssinatura}")
    private String chaveAssinatura;

    public static void main(String[] args) {
        ConfigurableApplicationContext contexto = SpringApplication.run(VendasApiApplication.class);
        JwtService service = contexto.getBean(JwtService.class);
        Usuario usuario = Usuario.builder().login("fulano").build();
        String token = service.gerarToken(usuario);
        System.out.println(token);
    }

    public String gerarToken(Usuario usuario) {
        long minutesExpire = Long.valueOf(this.expiracao);
        LocalDateTime plusMinutes = LocalDateTime.now().plusMinutes(minutesExpire);
        Instant toInstant = plusMinutes.atZone(ZoneId.systemDefault()).toInstant();
        Date from = Date.from(toInstant);
        return Jwts
                .builder()
                .setSubject(usuario.getLogin())
                .setExpiration(from)
                .signWith(SignatureAlgorithm.HS512, chaveAssinatura)
                .compact();
    }
}
