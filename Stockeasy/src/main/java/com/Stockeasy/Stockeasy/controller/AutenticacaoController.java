package com.Stockeasy.Stockeasy.controller;

import com.Stockeasy.Stockeasy.model.Usuario;
import com.Stockeasy.Stockeasy.config.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/login")
public class AutenticacaoController {
    //utilizando authentication manager do spring para autenticar o usuario
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<?> logar(@RequestBody Map<String, String> body){
        String login = body.get("login");
        String senha = body.get("senha");

        var token = new UsernamePasswordAuthenticationToken(login, senha);
        var authentication = authenticationManager.authenticate(token);

        String JWT = tokenService.geraToken((Usuario) authentication.getPrincipal());
        Map<String, String> resposta = new HashMap<>();
        resposta.put("token", JWT);
        return ResponseEntity.ok(resposta);
    }
}
