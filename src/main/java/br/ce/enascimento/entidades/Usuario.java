package br.ce.enascimento.entidades;

import java.util.Objects;

public class Usuario implements Pessoa{

    private String nome;

    public Usuario() {
    }

    public Usuario(String nome) {
        this.nome = nome;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(nome, usuario.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public boolean isVivo() {
        return true;
    }

    @Override
    public String toString() {
        return "Usuario com nome=" + nome;
    }
}
