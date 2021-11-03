package br.ce.enascimento.service;

import br.ce.enascimento.entidades.Usuario;

public interface SendEmailService {

    public void notificarAtraso(Usuario usuario);
}
