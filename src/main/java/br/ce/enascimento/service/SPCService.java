package br.ce.enascimento.service;

import br.ce.enascimento.entidades.Usuario;

public interface SPCService {

    boolean possuiNegativacao(Usuario usuario) throws Exception;
}
