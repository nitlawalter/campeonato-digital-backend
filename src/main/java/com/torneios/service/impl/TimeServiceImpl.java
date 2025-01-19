package com.torneios.service.impl;

import com.torneios.model.Time;
import com.torneios.repository.TimeRepository;
import com.torneios.service.TimeService;
import com.torneios.exception.NegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeServiceImpl implements TimeService {

    private final TimeRepository timeRepository;

    @Override
    @Transactional
    public Time criar(Time time) {
        validarTime(time);
        
        if (timeRepository.existsByNomeAndJogador(time.getNome(), time.getJogador())) {
            throw new NegocioException("Já existe um time com este nome para este jogador");
        }

        return timeRepository.save(time);
    }

    @Override
    @Transactional
    public Time atualizar(Long id, Time time) {
        Time timeExistente = buscarPorId(id);
        validarTime(time);
        
        // Verifica se o novo nome já existe para outro time do mesmo jogador
        if (!timeExistente.getNome().equals(time.getNome()) && 
            timeRepository.existsByNomeAndJogador(time.getNome(), time.getJogador())) {
            throw new NegocioException("Já existe um time com este nome para este jogador");
        }

        timeExistente.setNome(time.getNome());
        timeExistente.setJogador(time.getJogador());
        timeExistente.setEmblema(time.getEmblema());

        return timeRepository.save(timeExistente);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Time time = buscarPorId(id);
        // Aqui poderíamos adicionar verificações adicionais
        // Por exemplo, verificar se o time não está inscrito em nenhum campeonato
        timeRepository.delete(time);
    }

    @Override
    public Time buscarPorId(Long id) {
        return timeRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Time não encontrado"));
    }

    @Override
    public List<Time> listarTodos() {
        return timeRepository.findAll();
    }

    private void validarTime(Time time) {
        if (time.getNome() == null || time.getNome().trim().isEmpty()) {
            throw new NegocioException("Nome do time é obrigatório");
        }
        
        if (time.getJogador() == null || time.getJogador().trim().isEmpty()) {
            throw new NegocioException("Nome do jogador é obrigatório");
        }
        
        // Remove espaços em branco extras
        time.setNome(time.getNome().trim());
        time.setJogador(time.getJogador().trim());
        
        if (time.getEmblema() != null) {
            time.setEmblema(time.getEmblema().trim());
        }
    }
} 