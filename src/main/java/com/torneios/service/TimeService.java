package com.torneios.service;

import com.torneios.dto.TimeDTO;
import java.util.List;

public interface TimeService {
    TimeDTO criar(TimeDTO timeDTO);
    TimeDTO atualizar(Long id, TimeDTO timeDTO);
    void excluir(Long id);
    TimeDTO buscarPorId(Long id);
    List<TimeDTO> listar();
} 