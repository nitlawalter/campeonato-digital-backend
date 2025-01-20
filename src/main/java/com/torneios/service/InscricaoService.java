package com.torneios.service;

import com.torneios.dto.InscricaoDTO;
import java.util.List;

public interface InscricaoService {
    InscricaoDTO inscrever(Long campeonatoId, Long timeId);
    void aprovarInscricao(Long inscricaoId);
    void reprovarInscricao(Long inscricaoId);
    List<InscricaoDTO> listarPorCampeonato(Long campeonatoId);
    List<InscricaoDTO> listarInscricoesAprovadas(Long campeonatoId);
} 