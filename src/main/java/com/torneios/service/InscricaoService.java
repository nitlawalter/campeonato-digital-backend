package com.torneios.service;

import com.torneios.model.Inscricao;
import java.util.List;

public interface InscricaoService {
    Inscricao inscrever(Long campeonatoId, Long timeId);
    void aprovarInscricao(Long inscricaoId);
    void reprovarInscricao(Long inscricaoId);
    List<Inscricao> listarPorCampeonato(Long campeonatoId);
    List<Inscricao> listarInscricoesAprovadas(Long campeonatoId);
} 