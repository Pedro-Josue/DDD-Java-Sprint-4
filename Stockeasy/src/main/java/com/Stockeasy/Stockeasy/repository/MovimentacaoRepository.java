package com.Stockeasy.Stockeasy.repository;

import com.Stockeasy.Stockeasy.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
}
