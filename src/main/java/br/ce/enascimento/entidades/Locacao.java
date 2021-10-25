package br.ce.enascimento.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Locacao {

    private Usuario usuario;
    private Filme filme;
    private Date dataLocacao;
    private Date dataRetorno;
    private Double valor;


    public Usuario getUsuario() {
        return usuario;
    }

    public Locacao setUsuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public Filme getFilme() {
        return filme;
    }

    public Locacao setFilme(Filme filme) {
        this.filme = filme;
        return this;
    }

    public Date getDataLocacao() {
        return dataLocacao;
    }

    public Locacao setDataLocacao(Date dataLocacao) {
        this.dataLocacao = dataLocacao;
        return this;
    }

    public Date getDataRetorno() {
        return dataRetorno;
    }

    public Locacao setDataRetorno(Date dataRetorno) {
        this.dataRetorno = dataRetorno;
        return this;
    }

    public Double getValor() {
        return valor;
    }

    public Locacao setValor(Double valor) {
        this.valor = valor;
        return this;
    }
}
