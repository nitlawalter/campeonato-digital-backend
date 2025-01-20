package com.torneios.service.impl;

import com.torneios.dto.TimeDTO;
import com.torneios.model.Time;
import com.torneios.repository.TimeRepository;
import com.torneios.service.TimeService;
import com.torneios.exception.NegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeServiceImpl implements TimeService {

    private final TimeRepository timeRepository;

    @Override
    @Transactional
    public TimeDTO criar(TimeDTO timeDTO) {
        validarTime(timeDTO);
        
        if (timeRepository.existsByNomeAndAbreviacao(timeDTO.getNome(), timeDTO.getAbreviacao())) {
            throw new NegocioException("Já existe um time com este nome e abreviação");
        }

        Time time = toEntity(timeDTO);
        return toDTO(timeRepository.save(time));
    }

    @Override
    @Transactional
    public TimeDTO atualizar(Long id, TimeDTO timeDTO) {
        Time timeExistente = timeRepository.findById(id)
            .orElseThrow(() -> new NegocioException("Time não encontrado"));
            
        validarTime(timeDTO);
        
        if (!timeExistente.getNome().equals(timeDTO.getNome()) && 
            timeRepository.existsByNomeAndAbreviacao(timeDTO.getNome(), timeDTO.getAbreviacao())) {
            throw new NegocioException("Já existe um time com este nome e abreviação");
        }

        timeExistente.setNome(timeDTO.getNome());
        timeExistente.setAbreviacao(timeDTO.getAbreviacao());
        timeExistente.setCidade(timeDTO.getCidade());
        timeExistente.setEstado(timeDTO.getEstado());
        timeExistente.setLogo(timeDTO.getLogo());

        return toDTO(timeRepository.save(timeExistente));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Time time = timeRepository.findById(id)
            .orElseThrow(() -> new NegocioException("Time não encontrado"));
        timeRepository.delete(time);
    }

    @Override
    public TimeDTO buscarPorId(Long id) {
        return toDTO(timeRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Time não encontrado")));
    }

    @Override
    public List<TimeDTO> listar() {
        return timeRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private void validarTime(TimeDTO timeDTO) {
        if (timeDTO.getNome() == null || timeDTO.getNome().trim().isEmpty()) {
            throw new NegocioException("Nome do time é obrigatório");
        }
        if (timeDTO.getAbreviacao() == null || timeDTO.getAbreviacao().trim().isEmpty()) {
            throw new NegocioException("Abreviação do time é obrigatória");
        }
        if (timeDTO.getCidade() == null || timeDTO.getCidade().trim().isEmpty()) {
            throw new NegocioException("Cidade do time é obrigatória");
        }
        if (timeDTO.getEstado() == null || timeDTO.getEstado().trim().isEmpty()) {
            throw new NegocioException("Estado do time é obrigatório");
        }
    }

    private TimeDTO toDTO(Time time) {
        TimeDTO dto = new TimeDTO();
        dto.setId(time.getId());
        dto.setNome(time.getNome());
        dto.setAbreviacao(time.getAbreviacao());
        dto.setCidade(time.getCidade());
        dto.setEstado(time.getEstado());
        dto.setLogo(time.getLogo());
        return dto;
    }

    private Time toEntity(TimeDTO dto) {
        Time time = new Time();
        time.setNome(dto.getNome());
        time.setAbreviacao(dto.getAbreviacao());
        time.setCidade(dto.getCidade());
        time.setEstado(dto.getEstado());
        time.setLogo(dto.getLogo());
        return time;
    }
} 