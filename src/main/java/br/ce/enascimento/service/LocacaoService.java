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

public class LocacaoService {

    private LocacaoDAO dao;
    private SCPService scpService;
    private SendEmailService enviarEmail;

    public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {

        if(filmes == null|| filmes.isEmpty()) throw new LocadoraException("Filmes não podem ser vazio!");
        Locacao locacao = new Locacao(filmes);
        if(usuario == null) throw new LocadoraException("Usuário não pode ser vazio!");
        if(locacao.filmeSemEstoque()) throw new FilmeSemEstoqueException();

        locacao.setFilmes(filmes);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());
        locacao.setValorTotal();

        Date dataEntrega = new Date();
        dataEntrega = adicionarDias(dataEntrega, 1);
        if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY))
            dataEntrega = adicionarDias(dataEntrega, 1);
        locacao.setDataRetorno(dataEntrega);

        if(scpService.possuiNegativacao(usuario)) throw new LocadoraException("Usuário negativado!");

        dao.salvar(locacao);

        return locacao;
    }

    public void notificarAtrasos(){
        List<Locacao> locacaos = dao.oberterLocacoesPendentes();
        locacaos.forEach(this::sendEmail);
    }

    public void setDao(LocacaoDAO dao) {
        this.dao = dao;
    }

    public void setScpService(SCPService scpService) {
        this.scpService = scpService;
    }

    private void sendEmail(Locacao locacao) {
        enviarEmail.notificarAtraso(locacao.getUsuario());
    }

    public void setEnviarEmail(SendEmailService enviarEmail) {
        this.enviarEmail = enviarEmail;
    }
}
