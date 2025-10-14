package com.Stockeasy.Stockeasy.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {
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

    //metodos da userDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
