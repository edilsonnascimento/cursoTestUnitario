package br.ce.enascimento.builders;

import br.ce.enascimento.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    private UsuarioBuilder(){};

    public static UsuarioBuilder umUsuario(){
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario();
        builder.usuario.setNome("Usuário 1");
        return builder;
    }

    public Usuario controi(){
        return usuario;
    }

}
