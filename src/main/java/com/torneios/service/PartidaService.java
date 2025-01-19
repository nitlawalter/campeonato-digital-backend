package com.torneios.service;

import com.torneios.model.Partida;
import java.util.List;

public interface PartidaService {
    Partida registrarResultado(Long partidaId, Integer golsTimeCasa, Integer golsTimeVisitante);
    List<Partida> listarPorFase(Long faseId);
    Partida buscarPorId(Long id);
} 