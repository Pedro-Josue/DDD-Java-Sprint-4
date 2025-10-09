package com.Stockeasy.Stockeasy.service;

import com.Stockeasy.Stockeasy.model.Material;
import com.Stockeasy.Stockeasy.model.Movimentacao;
import com.Stockeasy.Stockeasy.model.Tipo;
import com.Stockeasy.Stockeasy.repository.MaterialRepository;
import com.Stockeasy.Stockeasy.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MovimentacaoService {
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    public List<Movimentacao> listaMovimentacao() {
        return movimentacaoRepository.findAll();
    }

    public ResponseEntity<String> registraEntrada(Long id, Movimentacao movimentacao) {
        Optional<Material> materialOptional = materialRepository.findById(id);
        if (materialOptional.isPresent()){
            Material existente = materialOptional.get();
            existente.setQuantidade(existente.getQuantidade() + movimentacao.getQuantidade());
            materialRepository.save(existente);
            movimentacao.setMaterial(existente);
            movimentacao.setDataHora(LocalDateTime.now());
            movimentacao.setTipo(Tipo.ENTRADA);
            movimentacaoRepository.save(movimentacao);
            return ResponseEntity.status(201).body("Movimentação registrada com sucesso");
        } else {
            return ResponseEntity.status(404).body("O material não existe");
        }
    }

    public ResponseEntity<String> registraSaida(Long id, Movimentacao movimentacao) {
        Optional<Material> materialOptional = materialRepository.findById(id);
        if (materialOptional.isPresent()){
            Material existente = materialOptional.get();
            if (existente.getQuantidade() - movimentacao.getQuantidade() < 0){
                return ResponseEntity.status(400).body("A quantidade desejada para retirada é maior que a existente em estoque");
            } else {
                existente.setQuantidade(existente.getQuantidade() - movimentacao.getQuantidade());
                materialRepository.save(existente);
                movimentacao.setMaterial(existente);
                movimentacao.setDataHora(LocalDateTime.now());
                movimentacao.setTipo(Tipo.SAIDA);
                movimentacaoRepository.save(movimentacao);
                return ResponseEntity.status(201).body("Movimentação registrada com sucesso");
            }
        } else {
            return ResponseEntity.status(404).body("O material não existe");
        }
    }
}
