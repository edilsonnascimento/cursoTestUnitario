package br.ce.enascimento.service;

import br.ce.enascimento.entidades.Funcionario;
import br.ce.enascimento.entidades.Pessoa;
import br.ce.enascimento.entidades.Usuario;
import br.ce.enascimento.runners.ParalleRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class AssertsTest {

    @Test
    public void testes(){
        //True or false
        assertTrue(true);
        assertFalse(false);

        //Equals int
        assertEquals(1, 1);

        //Equals double
        assertEquals(1.65226, 1.6522, 0.001);
        assertEquals(Math.PI, 3.14, 0.01);

        //Equals Wrapper
        int inteiro = 5;
        Integer wrapperInteiro = 5;
        assertEquals(Integer.valueOf(inteiro), wrapperInteiro);
        assertEquals(inteiro, wrapperInteiro.intValue());

        //Equals string
        assertEquals("bola", "bola");
        assertTrue("bola".equalsIgnoreCase("Bola"));
        assertTrue("bola".startsWith("b"));
        //negacao
        assertNotEquals("bola", "Bola");

        //Equals Objetos
        //Comparação pelos métodos equals e hashCode
        Usuario usuario1 = new Usuario("Usuáro 1");
        Usuario usuario2 = new Usuario("Usuáro 1");
        assertEquals(usuario1, usuario2);


        //comparando instácias
        assertSame(usuario1, usuario1);
        //negacao
        assertNotSame(usuario1, usuario2);
        Pessoa usuario3 = new Usuario("Usuario3");
        Pessoa funcionario = new Funcionario();
        assertTrue(usuario3 instanceof Pessoa);
        assertTrue(funcionario instanceof Pessoa);

        //Valors nulos
        Usuario usuarioNulo = null;
        assertNull(usuarioNulo);
        //Negação
        Usuario usuario = new Usuario("Usuario não Nulo");
        assertNotNull(usuario);

        //AssertThat
        assertEquals("bola", "bola");
        assertThat("bola".equals("bola"), is(true));

    }
}
