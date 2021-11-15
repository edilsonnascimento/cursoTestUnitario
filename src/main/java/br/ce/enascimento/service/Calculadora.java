package br.ce.enascimento.service;

import java.math.BigDecimal;

public class Calculadora {


    public BigDecimal somar(BigDecimal valor1, BigDecimal valor2) {
        return valor1.add(valor2);
    }

    public BigDecimal subtrair(BigDecimal valor1, BigDecimal valor2) {
        return valor1.subtract(valor2);
    }

    public BigDecimal multiplicar(BigDecimal valor1, BigDecimal valor2) {
        return valor1.multiply(valor2);
    }

    public BigDecimal dividir(BigDecimal valor1, BigDecimal valor2) {
        return valor1.divide(valor2);
    }
}
