package br.ce.enascimento.entidades;

public class Filme {
    private String nome;
    private Integer estoque;
    private Double precoLocacao;

    public Filme() {}

    public Filme(String nome, Integer estoque, Double precoLocacao) {
        this.nome = nome;
        this.estoque = estoque;
        this.precoLocacao = precoLocacao;
    }

    public String getNome() {
        return nome;
    }
    public Integer getEstoque() {
        return estoque;
    }
    public Double getPrecoLocacao() {
        return precoLocacao;
    }
}
