package br.ce.enascimento.builders;

import br.ce.enascimento.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    public static FilmeBuilder umFilme(){
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme("Tema Filme", 2, 4.0);
        return builder;
    }
    public static FilmeBuilder umFilmeSemEstoque(){
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme("Tema Filme", 0, 4.0);
        return builder;
    }

    public Filme constroi(){
        return filme;
    }
}
