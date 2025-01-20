package com.torneios.service;

import com.torneios.dto.CampeonatoDTO;
import com.torneios.model.Campeonato;
import java.util.List;

public interface CampeonatoService {
    CampeonatoDTO criar(CampeonatoDTO campeonatoDTO);
    Campeonato atualizar(Long id, Campeonato campeonato);
    void deletar(Long id);
    Campeonato buscarPorId(Long id);
    List<Campeonato> listarTodos();
    void iniciarInscricoes(Long id);
    void encerrarInscricoes(Long id);
    void iniciarCampeonato(Long id);
    void finalizarCampeonato(Long id);
} 