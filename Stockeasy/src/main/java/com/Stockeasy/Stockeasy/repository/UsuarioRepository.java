package com.Stockeasy.Stockeasy.repository;

import com.Stockeasy.Stockeasy.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);
}
