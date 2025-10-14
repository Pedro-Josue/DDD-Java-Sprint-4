package com.Stockeasy.Stockeasy.config.security;

import com.Stockeasy.Stockeasy.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    //algoritimo para geraçao do token jwt, utilizando a biblioteca auth0
    public String geraToken(Usuario usuario) {
        try {
            var algoritimo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("Stockeasy")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracaoToken())
                    .sign(algoritimo);
        } catch (
                JWTCreationException exception) {
                    throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    //definindo a data de expiração do token
    private Instant dataExpiracaoToken() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
