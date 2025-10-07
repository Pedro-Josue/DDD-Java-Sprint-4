package com.Stockeasy.Stockeasy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Material")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private int quantidade;
    @ManyToOne
    private Categoria categoria;

    //construtor
    public Material(String nome, int quantidade, Categoria categoria) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }

    //construtor default
    public Material (){}

    //getters
    public Long getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public int getQuantidade() {
        return quantidade;
    }
    public Categoria getCategoria() {
        return categoria;
    }

    //setters
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
