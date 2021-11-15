package br.ce.enascimento.service;

import br.ce.enascimento.runners.ParalleRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(ParalleRunner.class)
public class CalculadoraTest {

    private Calculadora calculadora;

    @Before
    public void setup(){
        calculadora = new Calculadora();
        System.out.println("inicializado...");
    }
    @After
    public void tearDown(){
        System.out.println("finalizado..");
    }

    @Test
    public void deveSomarDoisValores(){
        //cenario
        //acao
        BigDecimal resultado = calculadora.somar(BigDecimal.TEN,BigDecimal.TEN);
        //avaliacao
        assertEquals(new BigDecimal("20"), resultado);
    }
    @Test
    public void deveSubtrairDoisVaores(){
        //cenario
        //acao
        BigDecimal resultado = calculadora.subtrair(BigDecimal.TEN, BigDecimal.ONE);
        //avaliacao
        assertEquals(new BigDecimal("9"), resultado);
    }
    @Test
    public void deveMultiplicarDoisVaores(){
        //cenario
        //acao
        BigDecimal resultado = calculadora.multiplicar(BigDecimal.TEN, BigDecimal.TEN);
        //avaliacao
        assertEquals(new BigDecimal("100"), resultado);
    }
    @Test
    public void deveDividirDoisVaores(){
        //cenario
        //acao
        BigDecimal resultado = calculadora.dividir(BigDecimal.TEN, BigDecimal.TEN);
        //avaliacao
        assertEquals(new BigDecimal("1"), resultado);
    }

}
