package br.ce.enascimento.service;

import br.ce.enascimento.entidades.Filme;
import br.ce.enascimento.entidades.Locacao;
import br.ce.enascimento.entidades.Usuario;
import br.ce.enascimento.utils.DataUtils;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {

    @Test
    public void main() {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Nome do Usuario");
        Filme filme = new Filme("Titulo Filme", 2, 5.0);

        //acao
        Locacao locacao = service.alugarFilme(usuario,filme);

        //verificacao
        assertThat(locacao.getValor(), is(equalTo(5.0)));
        assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
    }
}
