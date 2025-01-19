package com.torneios.service.impl;

import com.torneios.model.Inscricao;
import com.torneios.model.Campeonato;
import com.torneios.model.Time;
import com.torneios.model.enums.StatusCampeonato;
import com.torneios.repository.InscricaoRepository;
import com.torneios.service.InscricaoService;
import com.torneios.service.CampeonatoService;
import com.torneios.service.TimeService;
import com.torneios.exception.NegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InscricaoServiceImpl implements InscricaoService {

    private final InscricaoRepository inscricaoRepository;
    private final CampeonatoService campeonatoService;
    private final TimeService timeService;

    @Override
    @Transactional
    public Inscricao inscrever(Long campeonatoId, Long timeId) {
        Campeonato campeonato = campeonatoService.buscarPorId(campeonatoId);
        Time time = timeService.buscarPorId(timeId);
        
        if (!campeonato.getStatus().equals(StatusCampeonato.INSCRICOES_ABERTAS)) {
            throw new NegocioException("Campeonato não está com inscrições abertas");
        }

        if (inscricaoRepository.findByCampeonatoAndTime(campeonato, time).isPresent()) {
            throw new NegocioException("Time já está inscrito neste campeonato");
        }

        long totalInscritos = inscricaoRepository.countByCampeonatoAndAprovada(campeonato, true);
        if (totalInscritos >= campeonato.getNumeroMaximoTimes()) {
            throw new NegocioException("Número máximo de times já foi atingido");
        }

        Inscricao inscricao = new Inscricao();
        inscricao.setCampeonato(campeonato);
        inscricao.setTime(time);
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setAprovada(false);

        return inscricaoRepository.save(inscricao);
    }

    @Override
    @Transactional
    public void aprovarInscricao(Long inscricaoId) {
        Inscricao inscricao = buscarInscricao(inscricaoId);
        
        if (inscricao.getAprovada()) {
            throw new NegocioException("Inscrição já está aprovada");
        }

        if (!inscricao.getCampeonato().getStatus().equals(StatusCampeonato.INSCRICOES_ABERTAS)) {
            throw new NegocioException("Campeonato não está com inscrições abertas");
        }

        long totalAprovados = inscricaoRepository.countByCampeonatoAndAprovada(inscricao.getCampeonato(), true);
        if (totalAprovados >= inscricao.getCampeonato().getNumeroMaximoTimes()) {
            throw new NegocioException("Número máximo de times já foi atingido");
        }

        inscricao.setAprovada(true);
        inscricaoRepository.save(inscricao);
    }

    @Override
    @Transactional
    public void reprovarInscricao(Long inscricaoId) {
        Inscricao inscricao = buscarInscricao(inscricaoId);
        
        if (inscricao.getAprovada()) {
            throw new NegocioException("Não é possível reprovar uma inscrição já aprovada");
        }

        inscricaoRepository.delete(inscricao);
    }

    @Override
    public List<Inscricao> listarPorCampeonato(Long campeonatoId) {
        Campeonato campeonato = campeonatoService.buscarPorId(campeonatoId);
        return inscricaoRepository.findByCampeonato(campeonato);
    }

    @Override
    public List<Inscricao> listarInscricoesAprovadas(Long campeonatoId) {
        Campeonato campeonato = campeonatoService.buscarPorId(campeonatoId);
        return inscricaoRepository.findByCampeonatoAndAprovada(campeonato, true);
    }

    private Inscricao buscarInscricao(Long id) {
        return inscricaoRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Inscrição não encontrada"));
    }
} 