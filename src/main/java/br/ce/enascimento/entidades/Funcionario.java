package br.ce.enascimento.entidades;

public class Funcionario implements Pessoa{

    private double salario;

    @Override
    public boolean isVivo() {
        return true;
    }
}
