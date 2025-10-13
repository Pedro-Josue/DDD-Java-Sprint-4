package com.Stockeasy.Stockeasy.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Movimentacao")
public class Movimentacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    @ManyToOne
    private Usuario usuario;
    private int quantidade;
    private LocalDateTime dataHora;
    @ManyToOne
    private Material material;

    //construtor
    public Movimentacao(Tipo tipo, Usuario usuario, int quantidade, LocalDateTime dataHora, Material material) {
        this.tipo = tipo;
        this.usuario = usuario;
        this.quantidade = quantidade;
        this.dataHora = dataHora;
        this.material = material;
    }

    //construtor default
    public Movimentacao() {}

    //getters
    public Long getId() {
        return id;
    }
    public Tipo getTipo() {
        return tipo;
    }
    public int getQuantidade() {
        return quantidade;
    }
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    public Material getMaterial() {
        return material;
    }
    public Usuario getUsuario() {
        return usuario;
    }

    //setters
    public void setMaterial(Material material) {
        this.material = material;
    }
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
