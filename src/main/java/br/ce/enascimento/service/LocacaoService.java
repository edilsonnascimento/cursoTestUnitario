package br.ce.enascimento.service;

import br.ce.enascimento.dao.LocacaoDAO;
import br.ce.enascimento.entidades.Filme;
import br.ce.enascimento.entidades.Locacao;
import br.ce.enascimento.entidades.Usuario;
import br.ce.enascimento.exception.FilmeSemEstoqueException;
import br.ce.enascimento.exception.LocadoraException;
import br.ce.enascimento.utils.DataUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.enascimento.utils.DataUtils.adicionarDias;
import static br.ce.enascimento.utils.DataUtils.obterDataComDiferencaDias;

public class LocacaoService {

    private LocacaoDAO dao;
    private SPCService spcService;
    private SendEmailService enviarEmail;
    private Locacao locacao;

    public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {

        if(filmes == null|| filmes.isEmpty()) throw new LocadoraException("Filmes não podem ser vazio!");
        locacao = new Locacao(filmes);
        if(usuario == null) throw new LocadoraException("Usuário não pode ser vazio!");
        if(locacao.filmeSemEstoque()) throw new FilmeSemEstoqueException();

        locacao.setFilmes(filmes);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(getNovaData());
        locacao.setValorTotal();

        locacao.setDataRetorno(calculaDataEntrega());

        boolean negativado;
        try {
            negativado = spcService.possuiNegativacao(usuario);
        } catch (Exception e) {
            throw  new LocadoraException("Serviço SPC indisponível tente mais tarde.");
        }
        if(negativado) throw new LocadoraException("Usuário negativado!");

        dao.salvar(locacao);

        return locacao;
    }

    private Date calculaDataEntrega() {
        Date dataEntrega = getNovaData();
        dataEntrega = adicionarDias(dataEntrega, 1);
        if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY))
            dataEntrega = adicionarDias(dataEntrega, 1);
        return dataEntrega;
    }

    protected Date getNovaData() {
        return new Date();
    }

    public void notificarAtrasos(){
        List<Locacao> locacaos = dao.oberterLocacoesPendentes();
        locacaos.forEach(locacao -> {
            if(locacao.getDataRetorno().before(getNovaData()))
                enviarEmail.notificarAtraso(locacao.getUsuario());
        });
    }

    public void prorrogarLocacao(Locacao locacao, Integer quantidadeDias){
        Locacao novaLocacao = new Locacao();
        novaLocacao.setUsuario(locacao.getUsuario());
        novaLocacao.setFilmes(locacao.getFilmes());
        novaLocacao.setDataLocacao(locacao.getDataLocacao());
        novaLocacao.setDataRetorno(obterDataComDiferencaDias(quantidadeDias));
        novaLocacao.setValorTotal(locacao.getValorTotal() * quantidadeDias);
        dao.salvar(novaLocacao);
    }
}
