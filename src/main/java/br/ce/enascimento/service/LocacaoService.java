package br.ce.enascimento.service;

import br.ce.enascimento.entidades.Filme;
import br.ce.enascimento.entidades.Locacao;
import br.ce.enascimento.entidades.Usuario;

import java.util.Date;

import static br.ce.enascimento.utils.DataUtils.adicionarDias;

public class LocacaoService {

    public Locacao alugarFilme(Usuario usuario, Filme filme) throws Exception {

        if(filme.getEstoque() == 0){
            throw new Exception("Filme não tem estoque");
        }
        Locacao locacao = new Locacao();
        locacao.setFilme(filme);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());
        locacao.setValor(filme.getPrecoLocacao());

        Date dataEntrega = new Date();
        dataEntrega = adicionarDias(dataEntrega, 1);
        locacao.setDataRetorno(dataEntrega);

        return locacao;
    }

}
