package br.ce.enascimento.entidades;

public class Desconto {


    private double valorLocacao;
    private int quantidadeFilme;

    public double descontar(double valorLocacao, int quantidadeFilme){
        this.valorLocacao = valorLocacao;
        this.quantidadeFilme =quantidadeFilme;
        return valorDescontoTerceiroFilme();
    }
    private double valorDescontoTerceiroFilme(){
           return quantidadeFilme == 2 ? valorLocacao * 0.25 : valorDescontoQuartoFilme();
    }

    private double valorDescontoQuartoFilme(){
        return quantidadeFilme == 3 ? valorLocacao * 0.50 : valorDescontoQuintoFilmes();
    }

    private double valorDescontoQuintoFilmes() {
        return quantidadeFilme == 4 ? valorLocacao * 0.75 : valorDescontoSextoFilmes();
    }

    private double valorDescontoSextoFilmes() {
        return quantidadeFilme == 5 ? valorLocacao : 0;
    }
}
