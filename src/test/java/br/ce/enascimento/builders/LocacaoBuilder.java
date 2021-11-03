package br.ce.enascimento.builders;

import br.ce.enascimento.entidades.Filme;
import br.ce.enascimento.entidades.Usuario;
import java.util.Arrays;
import java.lang.Double;
import java.util.Date;
import br.ce.enascimento.entidades.Locacao;
import br.ce.enascimento.utils.DataUtils;


public class LocacaoBuilder {

    private Locacao locacao;
    private LocacaoBuilder(){}

    public static LocacaoBuilder umLocacao() {
        LocacaoBuilder builder = new LocacaoBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    public static void inicializarDadosPadroes(LocacaoBuilder builder) {
        builder.locacao = new Locacao();
        Locacao locacao = builder.locacao;


        locacao.setUsuario(UsuarioBuilder.umUsuario().controi());
        locacao.setFilmes(Arrays.asList(FilmeBuilder.umFilme().constroi()));
        locacao.setDataLocacao(new Date());
        locacao.setDataRetorno(DataUtils.adicionarDias(new Date(), 1));
        locacao.setValorTotal();
    }

    public LocacaoBuilder comUsuario(Usuario param) {
        locacao.setUsuario(param);
        return this;
    }

    public LocacaoBuilder comListaFilmes(Filme... params) {
        locacao.setFilmes(Arrays.asList(params));
        return this;
    }

    public LocacaoBuilder comDataLocacao(Date param) {
        locacao.setDataLocacao(param);
        return this;
    }

    public LocacaoBuilder comDataRetorno(Date param) {
        locacao.setDataRetorno(param);
        return this;
    }

    public LocacaoBuilder comValorTotal(Double param) {
        locacao.setValorTotal();
        return this;
    }

    public Locacao agora() {
        return locacao;
    }
}
