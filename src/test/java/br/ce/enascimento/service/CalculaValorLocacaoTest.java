package br.ce.enascimento.service;

import br.ce.enascimento.entidades.Filme;
import br.ce.enascimento.entidades.Locacao;
import br.ce.enascimento.entidades.Usuario;
import br.ce.enascimento.exception.FilmeSemEstoqueException;
import br.ce.enascimento.exception.LocadoraException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CalculaValorLocacaoTest {

    private LocacaoService service;

    @Parameter
    public List<Filme> filmes;

    @Parameter(value=1)
    public Double valorLocacao;

    @Parameter(value=2)
    public String descricaoTeste;

    private static Filme filme1 = new Filme("Titulo Filme1", 2, 4.0);
    private static Filme filme2 = new Filme("Titulo Filme2", 2, 4.0);
    private static Filme filme3 = new Filme("Titulo Filme3", 2, 4.0);
    private static Filme filme4 = new Filme("Titulo Filme4", 2, 4.0);
    private static Filme filme5 = new Filme("Titulo Filme5", 2, 4.0);
    private static Filme filme6 = new Filme("Titulo Filme6", 2, 4.0);
    private static Filme filme7 = new Filme("Titulo Filme7", 2, 4.0);


    @Before
    public void setup(){
        service = new LocacaoService();
    }

    @Parameters(name="{2}")
    public static Collection<Object[]> getParametros(){
        return Arrays.asList(new Object[][]{
            {Arrays.asList(filme1, filme2), 8.0, "Aluguel 2 filmes 0% Desconto."},
            {Arrays.asList(filme1, filme2,filme3), 11.0, "Aluguel 3 filmes 25% Desconto."},
            {Arrays.asList(filme1, filme2,filme3, filme4), 13.0, "Aluguel 4 filmes 50% Desconto."},
            {Arrays.asList(filme1, filme2,filme3, filme4,filme5), 14.0, "Aluguel 5 filmes 75% Desconto."},
            {Arrays.asList(filme1, filme2,filme3, filme4,filme5,filme6), 14.0, "Aluguel 6 filmes 100% Desconto."},
            {Arrays.asList(filme1, filme2,filme3, filme4,filme5,filme6, filme7), 18.0, "Aluguel 7 filmes 0% Desconto."}
        });
    }

    @Test
    public void deve_CacularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Nome do Usuario");
        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);
        //avaliacao
        assertThat(locacao.getValorTotal(), is(valorLocacao));
    }
}