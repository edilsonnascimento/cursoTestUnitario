package br.ce.enascimento.service;

import br.ce.enascimento.entidades.Filme;
import br.ce.enascimento.entidades.Locacao;
import br.ce.enascimento.entidades.Usuario;
import br.ce.enascimento.utils.DataUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Test
    public void testarLocacao() {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Nome do Usuario");
        Filme filme = new Filme("Titulo Filme", 2, 5.0);

        //acao
        Locacao locacao = service.alugarFilme(usuario,filme);

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(6.0)));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(false));
    }
}
