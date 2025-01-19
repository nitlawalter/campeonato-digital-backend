package com.torneios.service;

import com.torneios.model.Fase;
import java.util.List;

public interface FaseService {
    Fase criarFaseGrupos(Long campeonatoId);
    Fase criarProximaFase(Long campeonatoId);
    List<Fase> listarPorCampeonato(Long campeonatoId);
    void gerarPartidas(Long faseId);
} 