package br.ce.enascimento.service;

import br.ce.enascimento.builders.FilmeBuilder;
import br.ce.enascimento.builders.LocacaoBuilder;
import br.ce.enascimento.builders.UsuarioBuilder;
import br.ce.enascimento.dao.LocacaoDAO;
import br.ce.enascimento.dao.LocacaoImplementDAO;
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
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.enascimento.builders.LocacaoBuilder.*;
import static br.ce.enascimento.matchers.CoreMatcherProprio.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.mockito.Mockito.*;

public class LocacaoServiceTest {

    private LocacaoService service;
    private SCPService scpService;
    private LocacaoDAO dao;
    private SendEmailService enviarEmail;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup(){
        service = new LocacaoService();
        dao = mock(LocacaoImplementDAO.class);
        service.setDao(dao);
        scpService = mock(SCPService.class);
        service.setScpService(scpService);
        enviarEmail = mock(SendEmailService.class);
        service.setEnviarEmail(enviarEmail);
    }

    @Test
    public void deve_AlugarFilmes() {
        assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SUNDAY));

        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().controi();
        Filme filme1 = new FilmeBuilder().umFilme().constroi();
        Filme filme2 = new FilmeBuilder().umFilme().constroi();
        Filme filme3 = new FilmeBuilder().umFilme().constroi();
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
        Usuario usuario = UsuarioBuilder.umUsuario().controi();
        Filme filme = new FilmeBuilder().umFilmeSemEstoque().constroi();
        List filmes = Arrays.asList(filme);
        //acao
        service.alugarFilme(usuario,filmes);
    }

    @Test
    public void deve_LancarExpetion_Filmes_Vazio() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().controi();
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filmes não podem ser vazio!");
        //acao
        service.alugarFilme(usuario,null);
    }

    @Test
    public void deve_LancarExcepetion_UsuarioVazio() throws FilmeSemEstoqueException {
        //cenario
        Filme filme1 = new FilmeBuilder().umFilme().constroi();
        Filme filme2 = new FilmeBuilder().umFilme().constroi();
        Filme filme3 = new FilmeBuilder().umFilme().constroi();
        List filmes = Arrays.asList(filme1, filme2, filme3);

        //acao
        try {
            service.alugarFilme(null,filmes);
            fail();
        } catch (LocadoraException e) {
           assertThat(e.getMessage(), is("Usuário não pode ser vazio!"));
        }
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
        assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().controi();
        List<Filme> filmes = Arrays.asList(new FilmeBuilder().umFilme().constroi());
        //acao
        Locacao locacao = service.alugarFilme(usuario,filmes);
        //avaliacao
        assertThat(locacao.getDataRetorno(), caiNaSegunda());
    }

    @Test
    public void naoDeveAlugarFilmeParaUsuarioNegativado() throws FilmeSemEstoqueException{
        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().controi();
        List<Filme> filmes = Arrays.asList(new FilmeBuilder().umFilme().constroi());
        when(scpService.possuiNegativacao(usuario)).thenReturn(true);
        //acao
        try {
            Locacao locacao = service.alugarFilme(usuario,filmes);
            fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuário negativado!"));
        }

        verify(scpService).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesPendentes(){
        //cenario
        Usuario usuario1 = UsuarioBuilder.umUsuario().comNome("Usuario atrasado").controi();
        Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").controi();
        Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Outro Usuario atrasado").controi();
        List<Locacao> locacoes = Arrays.asList(
                umaLocacao().comAtraso().comUsuario(usuario1).constroi(),
                umaLocacao().comUsuario(usuario2).constroi(),
                umaLocacao().comAtraso().comUsuario(usuario3).constroi(),
                umaLocacao().comAtraso().comUsuario(usuario3).constroi());
        when(dao.oberterLocacoesPendentes()).thenReturn(locacoes);
        //acao
        service.notificarAtrasos();
        //verificacao
        verify(enviarEmail).notificarAtraso(usuario1);
        verify(enviarEmail, times(2)).notificarAtraso(usuario3);
        verify(enviarEmail, never()).notificarAtraso(usuario2);
        verify(enviarEmail, times(3)).notificarAtraso(any());
        verifyNoMoreInteractions(enviarEmail);
        verifyZeroInteractions(scpService);
    }
}
