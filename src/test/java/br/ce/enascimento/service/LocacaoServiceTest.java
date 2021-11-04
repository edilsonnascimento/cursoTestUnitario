package br.ce.enascimento.service;

import br.ce.enascimento.builders.FilmeBuilder;
import br.ce.enascimento.dao.LocacaoDAO;
import br.ce.enascimento.entidades.Filme;
import br.ce.enascimento.entidades.Locacao;
import br.ce.enascimento.entidades.Usuario;
import br.ce.enascimento.exception.FilmeSemEstoqueException;
import br.ce.enascimento.exception.LocadoraException;
import br.ce.enascimento.utils.DataUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.enascimento.builders.FilmeBuilder.umFilme;
import static br.ce.enascimento.builders.LocacaoBuilder.umaLocacao;
import static br.ce.enascimento.builders.UsuarioBuilder.*;
import static br.ce.enascimento.matchers.CoreMatcherProprio.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;

public class LocacaoServiceTest {

    @Mock
    private SPCService spcService;
    @Mock
    private LocacaoDAO dao;
    @Mock
    private SendEmailService enviarEmail;
    @InjectMocks
    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void deve_AlugarFilmes() {
        assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SUNDAY));

        //cenario
        Usuario usuario = umUsuario().controi();
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
        Usuario usuario = umUsuario().controi();
        Filme filme = new FilmeBuilder().umFilmeSemEstoque().constroi();
        List filmes = Arrays.asList(filme);
        //acao
        service.alugarFilme(usuario,filmes);
    }

    @Test
    public void deve_LancarExpetion_Filmes_Vazio() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = umUsuario().controi();
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
        Usuario usuario = umUsuario().controi();
        List<Filme> filmes = Arrays.asList(new FilmeBuilder().umFilme().constroi());
        //acao
        Locacao locacao = service.alugarFilme(usuario,filmes);
        //avaliacao
        assertThat(locacao.getDataRetorno(), caiNaSegunda());
    }

    @Test
    public void naoDeveAlugarFilmeParaUsuarioNegativado() throws Exception {
        //cenario
        Usuario usuario = umUsuario().controi();
        List<Filme> filmes = Arrays.asList(new FilmeBuilder().umFilme().constroi());
        when(spcService.possuiNegativacao(usuario)).thenReturn(true);
        //acao
        try {
            Locacao locacao = service.alugarFilme(usuario,filmes);
            fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuário negativado!"));
        }

        verify(spcService).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesPendentes(){
        //cenario
        Usuario usuario1 = umUsuario().comNome("Usuario atrasado").controi();
        Usuario usuario2 = umUsuario().comNome("Usuario em dia").controi();
        Usuario usuario3 = umUsuario().comNome("Outro Usuario atrasado").controi();
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
        verifyZeroInteractions(spcService);
    }

    @Test
    public void deveTratarExceptionDoServicoSCP() throws Exception {
        //cenario
        Usuario usuario = umUsuario().controi();
        List<Filme> filmes = Arrays.asList(umFilme().constroi());
        when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha no Serviço SPC!"));
        //verificacao
        exception.expect(LocadoraException.class);
        exception.expectMessage("Serviço SPC indisponível tente mais tarde.");
        //acao
        service.alugarFilme(usuario, filmes);
    }
}
