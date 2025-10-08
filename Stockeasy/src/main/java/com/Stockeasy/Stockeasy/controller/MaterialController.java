package com.Stockeasy.Stockeasy.controller;

import com.Stockeasy.Stockeasy.model.Material;
import com.Stockeasy.Stockeasy.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materiais")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    //lista todos os materiais
    @GetMapping
    public List<Material> listaMateriais(){
        return materialService.listaMateriais();
    }

    //cria material novo
    @PostMapping("/novo")
    public ResponseEntity<String> criaMaterial(@RequestBody Material material){
        return materialService.criaMaterial(material);
    }

    //editar material
    @PutMapping("/edita/{id}")
    public ResponseEntity<String> editaMaterial(@PathVariable Long id, @RequestBody Material material){
        return materialService.editaMaterial(id, material);
    }

    //exclui material
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluiMaterial(@PathVariable Long id){
        return materialService.excluiMaterial(id);
    }
}
