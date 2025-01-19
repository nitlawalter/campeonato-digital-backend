package com.torneios.service;

import com.torneios.model.Time;
import java.util.List;

public interface TimeService {
    Time criar(Time time);
    Time atualizar(Long id, Time time);
    void deletar(Long id);
    Time buscarPorId(Long id);
    List<Time> listarTodos();
} 