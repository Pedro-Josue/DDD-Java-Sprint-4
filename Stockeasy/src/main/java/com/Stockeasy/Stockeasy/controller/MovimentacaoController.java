package com.Stockeasy.Stockeasy.controller;

import com.Stockeasy.Stockeasy.model.Movimentacao;
import com.Stockeasy.Stockeasy.service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {
    @Autowired
    private MovimentacaoService movimentacaoService;

    //lista todas as movimentações
    @GetMapping
    public List<Movimentacao> listaMovimentacao(){
        return movimentacaoService.listaMovimentacao();
    }

    //registra uma nova entrada
    @PostMapping("/entrada/{id}")
    public ResponseEntity<String> registraEntrada(@PathVariable Long id, @RequestBody Movimentacao movimentacao){
        return movimentacaoService.registraEntrada(id, movimentacao);
    }

    //registra uma nova saída
    @PostMapping("saida/{id}")
    public ResponseEntity<String> registraSaida(@PathVariable Long id, @RequestBody Movimentacao movimentacao){
        return movimentacaoService.registraSaida(id, movimentacao);
    }
}
