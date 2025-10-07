package com.Stockeasy.Stockeasy.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @OneToMany(mappedBy = "Categoria")
    private List<Material> materiais;

    //construtor
    public Categoria(String nome, List<Material> materiais) {
        this.nome = nome;
        this.materiais = materiais;
    }

    //Construtor default
    public Categoria (){}

    //getters
    public Long getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public List<Material> getMateriais() {
        return materiais;
    }

    //setters
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setMateriais(List<Material> materiais) {
        this.materiais = materiais;
    }
}
