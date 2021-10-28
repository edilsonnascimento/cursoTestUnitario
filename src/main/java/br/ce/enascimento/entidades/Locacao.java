package br.ce.enascimento.entidades;

import java.util.Date;
import java.util.List;


public class Locacao {

    private Usuario usuario;
    private List<Filme> filmes;
    private Date dataLocacao;
    private Date dataRetorno;
    private Double valorTotal;

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
        for (int i = 0; i < filmes.size(); i++) {
            totalDesconto += desconto.descontar(filmes.get(i).getPrecoLocacao(), i);
        }
        return valorTotal - totalDesconto;
    }

    public boolean filmeSemEstoque(){
        int soma = 0;
        for (Filme filme : this.filmes)
            if(filme.getEstoque() == 0) soma += 1;
        return soma > 0;
    }
}
