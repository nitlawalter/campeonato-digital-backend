package com.torneios.service;

import com.torneios.dto.FaseDTO;
import java.util.List;

public interface FaseService {
    FaseDTO criar(FaseDTO faseDTO);
    FaseDTO atualizar(Long id, FaseDTO faseDTO);
    void excluir(Long id);
    FaseDTO buscarPorId(Long id);
    List<FaseDTO> listar();
    List<FaseDTO> listarPorCampeonato(Long campeonatoId);
    void iniciarFase(Long id);
    void finalizarFase(Long id);
    FaseDTO criarFaseGrupos(Long campeonatoId);
    FaseDTO criarProximaFase(Long campeonatoId);
    void gerarPartidas(Long faseId);
} 