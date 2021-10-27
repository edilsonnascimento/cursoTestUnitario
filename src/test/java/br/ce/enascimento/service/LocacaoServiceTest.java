package br.ce.enascimento.service;

import br.ce.enascimento.entidades.Filme;
import br.ce.enascimento.entidades.Locacao;
import br.ce.enascimento.entidades.Usuario;
import br.ce.enascimento.exception.FilmeSemEstoqueException;
import br.ce.enascimento.exception.LocadoraException;
import br.ce.enascimento.utils.DataUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testarLocacao() {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Nome do Usuario");
        Filme filme = new Filme("Titulo Filme", 2, 5.0);
        Locacao locacao = null;
        //acao
        try {
            locacao = service.alugarFilme(usuario,filme);
        } catch (Exception e) {
            e.printStackTrace();
            //verificacao
            error.checkThat(locacao.getValor(), is(equalTo(6.0)));
            error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
            error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(false));
        }
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void deveLancarExcepetionFilmeSemEstoque() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Nome do Usuario");
        Filme filme = new Filme("Titulo Filme", 0, 5.0);
        //acao
        service.alugarFilme(usuario,filme);
    }

    @Test
    public void deveLancarExpetionFilmeVazio() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Nome do Usuario");
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme não pode ser vazio!");
        //acao
        service.alugarFilme(usuario,null);
    }

    @Test
    public void deveLancarExcepetionUsuarioVazio() throws FilmeSemEstoqueException {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = null;
        Filme filme = new Filme("Titulo Filme", 0, 5.0);
        //acao
        try {
            service.alugarFilme(usuario,filme);
            fail();
        } catch (LocadoraException e) {
           assertThat(e.getMessage(), is("Usuário não pode ser vazio!"));
        }
    }
}
