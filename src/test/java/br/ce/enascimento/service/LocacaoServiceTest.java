package br.ce.enascimento.service;

import br.ce.enascimento.entidades.Filme;
import br.ce.enascimento.entidades.Locacao;
import br.ce.enascimento.entidades.Usuario;
import br.ce.enascimento.exception.FilmeSemEstoqueException;
import br.ce.enascimento.exception.LocadoraException;
import br.ce.enascimento.matchers.CoreMatcherProprio;
import br.ce.enascimento.matchers.DiaSemanaMatcher;
import br.ce.enascimento.utils.DataUtils;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.enascimento.matchers.CoreMatcherProprio.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class LocacaoServiceTest {

    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup(){
        service = new LocacaoService();
    }

    @Test
    public void deve_AlugarFilmes() {
        assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.WEDNESDAY));

        //cenario
        Usuario usuario = new Usuario("Nome do Usuario");
        Filme filme1 = new Filme("Titulo Filme1", 2, 5.0);
        Filme filme2 = new Filme("Titulo Filme2", 2, 4.0);
        Filme filme3 = new Filme("Titulo Filme3", 1, 3.0);
        List filmes = Arrays.asList(filme1, filme2, filme3);
        Locacao locacao = new Locacao(filmes);
        //acao
        try {
            locacao = service.alugarFilme(usuario,filmes);
        } catch (Exception e) {
            //verificacao
            error.checkThat(locacao.getValorTotal(), is(equalTo(12.0)));
            error.checkThat(locacao.getDataLocacao(), ehHoje());
            error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
        }
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void deve_LancarExcepetion_Filme_SemEstoque() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Nome do Usuario");
        Filme filme1 = new Filme("Titulo Filme1", 2, 5.0);
        Filme filme2 = new Filme("Titulo Filme2", 0, 4.0);
        Filme filme3 = new Filme("Titulo Filme3", 0, 3.0);
        List filmes = Arrays.asList(filme1, filme2, filme3);
        //acao
        service.alugarFilme(usuario,filmes);
    }

    @Test
    public void deve_LancarExpetion_Filmes_Vazio() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Nome do Usuario");
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filmes n??o podem ser vazio!");
        //acao
        service.alugarFilme(usuario,null);
    }

    @Test
    public void deve_LancarExcepetion_UsuarioVazio() throws FilmeSemEstoqueException {
        //cenario
        Filme filme1 = new Filme("Titulo Filme1", 2, 5.0);
        Filme filme2 = new Filme("Titulo Filme2", 2, 4.0);
        Filme filme3 = new Filme("Titulo Filme3", 2, 3.0);
        List filmes = Arrays.asList(filme1, filme2, filme3);

        //acao
        try {
            service.alugarFilme(null,filmes);
            fail();
        } catch (LocadoraException e) {
           assertThat(e.getMessage(), is("Usu??rio n??o pode ser vazio!"));
        }
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
        assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0));
        //acao
        Locacao locacao = service.alugarFilme(usuario,filmes);
        //avaliacao
        assertThat(locacao.getDataRetorno(), caiNaSegunda());
    }
}
