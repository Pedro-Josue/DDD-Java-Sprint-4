package com.Stockeasy.Stockeasy.service;

import com.Stockeasy.Stockeasy.model.Material;
import com.Stockeasy.Stockeasy.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    public List<Material> listaMateriais() {
        return materialRepository.findAll();
    }

    public ResponseEntity<String> criaMaterial(Material material) {
        materialRepository.save(material);
        return ResponseEntity.status(201).body("Material criado com sucesso");
    }

    public ResponseEntity<String> editaMaterial(Long id, Material material) {
        Optional<Material> materialOptional = materialRepository.findById(id);
        if (materialOptional.isPresent()){
            Material existente = materialOptional.get();
            existente.setNome(material.getNome());
            existente.setCategoria(material.getCategoria());
            existente.setQuantidade(material.getQuantidade());
            materialRepository.save(existente);
            return ResponseEntity.status(200).body("Material editada com sucesso");
        } else {
            return ResponseEntity.status(404).body("Material não encontrada");
        }
    }

    public ResponseEntity<String> excluiMaterial(Long id) {
        Optional<Material> materialOptional = materialRepository.findById(id);
        if (materialOptional.isPresent()){
            Material existente = materialOptional.get();
            materialRepository.delete(existente);
            return ResponseEntity.status(200).body("Material excluído com sucesso");
        } else {
            return ResponseEntity.status(404).body("Material não encontrado");
        }
    }
}
