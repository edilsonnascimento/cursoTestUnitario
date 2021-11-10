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
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static br.ce.enascimento.builders.FilmeBuilder.umFilme;
import static br.ce.enascimento.builders.LocacaoBuilder.umaLocacao;
import static br.ce.enascimento.builders.UsuarioBuilder.umUsuario;
import static br.ce.enascimento.matchers.CoreMatcherProprio.*;
import static br.ce.enascimento.utils.DataUtils.obterData;
import static br.ce.enascimento.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class LocacaoServiceSpyTest {

    @Mock
    private SPCService spcService;
    @Mock
    private LocacaoDAO dao;
    @Mock
    private SendEmailService enviarEmail;
    @InjectMocks @Spy
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
    public void deve_AlugarFilmes() throws Exception {
        //cenario
        Usuario usuario = umUsuario().controi();
        List<Filme> filmes = Arrays.asList(umFilme().constroi());
        //acao
        Locacao locacao = service.alugarFilme(usuario,filmes);
        //verificacao
        error.checkThat(locacao.getValorTotal(), is(equalTo(4.0)));
        error.checkThat(locacao.getDataLocacao(), ehHoje());
        error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
        //cenario
        doReturn(obterData(6,11,2021)).when(service).getNovaData();
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
    public void deveCalcularDataRetorno() throws Exception {
        //cenário
        Class<LocacaoService> clazz = LocacaoService.class;
        Method method = clazz.getDeclaredMethod("calculaDataEntrega");
        method.setAccessible(true);
        //ação
        Date dataRetorno = (Date) method.invoke(service);

        //verificação
        assertThat(DataUtils.isMesmaData(dataRetorno, obterDataComDiferencaDias(1)), is(true));

    }

}
