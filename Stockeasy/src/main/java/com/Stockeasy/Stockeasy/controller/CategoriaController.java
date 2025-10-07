package com.Stockeasy.Stockeasy.controller;

import com.Stockeasy.Stockeasy.model.Categoria;
import com.Stockeasy.Stockeasy.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    //listar todas as categorias
    @GetMapping
    public List<Categoria> listaCategorias(){
        return categoriaService.listaCategoria();
    }

    //criar categoria
    @PostMapping("/novo")
    public ResponseEntity<String> criaCategoria(@RequestBody Categoria categoria){
        return categoriaService.criaCategoria(categoria);
    }

    //editar categoria existente
    @PutMapping("/edita/{id}")
    public ResponseEntity<String> editaCategoria(@PathVariable Long id, @RequestBody Categoria categoria){
        return categoriaService.editaCategoria(id, categoria);
    }

    //exclui categoria
    @DeleteMapping("/exclui/{id}")
    public ResponseEntity<String> excluiCategoria(@PathVariable Long id){
        return categoriaService.excluiCategoria(id);
    }
}
