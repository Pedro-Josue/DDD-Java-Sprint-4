package com.Stockeasy.Stockeasy.controller;

import com.Stockeasy.Stockeasy.model.Usuario;
import com.Stockeasy.Stockeasy.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    //criar novo usuario
    @PostMapping("/novo")
    public ResponseEntity<String> criaUsuario(@RequestBody Usuario usuario) {
        return usuarioService.criaUsuario(usuario);
    }
}
