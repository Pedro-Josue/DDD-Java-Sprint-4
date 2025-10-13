package com.Stockeasy.Stockeasy.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String senha;
    @OneToMany(mappedBy = "usuario")
    private List<Movimentacao> movimentacoes;

    //construtor
    public Usuario(String email, String senha, List<Movimentacao> movimentacoes) {
        this.login = email;
        this.senha = senha;
        this.movimentacoes = movimentacoes;
    }

    //construtor default
    public Usuario(){}

    //getters
    public Long getId() {
        return id;
    }
    public String getLogin() {
        return login;
    }
    public String getSenha() {
        return senha;
    }
    public List<Movimentacao> getMovimentacoes() {
        return movimentacoes;
    }

    //setters
    public void setLogin(String login) {
        this.login = login;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public void setMovimentacoes(List<Movimentacao> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }
}
