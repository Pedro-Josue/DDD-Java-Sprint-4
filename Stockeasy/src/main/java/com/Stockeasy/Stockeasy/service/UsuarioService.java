package com.Stockeasy.Stockeasy.service;

import com.Stockeasy.Stockeasy.model.Usuario;
import com.Stockeasy.Stockeasy.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<String> criaUsuario(Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        usuarioRepository.save(usuario);
        return ResponseEntity.status(201).body("Usuario criado com sucesso!");
    }
}
