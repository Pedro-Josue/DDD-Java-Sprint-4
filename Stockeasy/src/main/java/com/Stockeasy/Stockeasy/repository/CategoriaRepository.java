package com.Stockeasy.Stockeasy.repository;

import com.Stockeasy.Stockeasy.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
