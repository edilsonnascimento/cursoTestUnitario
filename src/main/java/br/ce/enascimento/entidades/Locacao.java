package br.ce.enascimento.entidades;

import java.util.Date;
import java.util.List;


public class Locacao {

    private static final int ZERO = 0;
    private Usuario usuario;
    private List<Filme> filmes;
    private Date dataLocacao;
    private Date dataRetorno;
    private Double valorTotal;

    public Locacao() {
    }

    public Locacao(List<Filme> filmes) {
        this.filmes = filmes;
    }

    public Locacao setUsuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public Locacao setFilmes(List<Filme> filmes) {
        this.filmes = filmes;
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

    public void setValorTotal() {
        this.valorTotal = filmes.stream().mapToDouble(Filme::getPrecoLocacao).sum();
    }

    public Double getValorTotal() {
        return aplicarDesconto();
    }

    private Double aplicarDesconto() {
        Desconto desconto = new Desconto();
        double totalDesconto=0;
        for (int posicao = 0; posicao < filmes.size(); posicao++)
            totalDesconto += desconto.descontar(filmes.get(posicao).getPrecoLocacao(), posicao);
        return valorTotal - totalDesconto;
    }

    public boolean filmeSemEstoque(){
        return ZERO < filmes.stream()
                .filter(filme -> filme.getEstoque() == ZERO)
                .mapToInt(Filme::getEstoque)
                .count();
    }
}
