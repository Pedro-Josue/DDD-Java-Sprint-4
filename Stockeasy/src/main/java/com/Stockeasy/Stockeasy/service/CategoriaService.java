package com.Stockeasy.Stockeasy.service;

import com.Stockeasy.Stockeasy.model.Categoria;
import com.Stockeasy.Stockeasy.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listaCategoria() {
        return categoriaRepository.findAll();
    }

    public ResponseEntity<String> criaCategoria(Categoria categoria) {
        categoriaRepository.save(categoria);
        return ResponseEntity.status(201).body("Categoria criada com sucesso");
    }

    public ResponseEntity<String> editaCategoria(Long id, Categoria categoria) {
        Optional<Categoria> categoriaAntiga = categoriaRepository.findById(id);
        if (categoriaAntiga.isPresent()){
            Categoria existente = categoriaAntiga.get();
            existente.setNome(categoria.getNome());
            categoriaRepository.save(existente);
            return ResponseEntity.status(200).body("Alterações salvas com sucesso");
        } else {
            return ResponseEntity.status(404).body("Categoria não encontrada");
        }
    }

    public ResponseEntity<String> excluiCategoria(Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isPresent()){
            Categoria existente = categoria.get();
            if (existente.getMateriais() != null && !existente.getMateriais().isEmpty()) {
                return ResponseEntity.status(400).body("Não é possível excluir uma categoria que possui materiais associados.");
            }
            categoriaRepository.delete(existente);
            return ResponseEntity.status(200).body("Categoria excluída com sucesso");
        } else {
            return ResponseEntity.status(404).body("Categoria não encontrada");
        }
    }
}
