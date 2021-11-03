package br.ce.enascimento.dao;

import br.ce.enascimento.entidades.Locacao;

import java.util.List;

public interface LocacaoDAO {

    void salvar(Locacao locacao);

    List<Locacao> oberterLocacoesPendentes();
}
