package com.torneios.service.impl;

import com.torneios.dto.FaseDTO;
import com.torneios.exception.NegocioException;
import com.torneios.model.Campeonato;
import com.torneios.model.Fase;
import com.torneios.model.enums.TipoFase;
import com.torneios.repository.CampeonatoRepository;
import com.torneios.repository.FaseRepository;
import com.torneios.service.FaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaseServiceImpl implements FaseService {

    private final FaseRepository faseRepository;
    private final CampeonatoRepository campeonatoRepository;

    @Override
    @Transactional
    public FaseDTO criar(FaseDTO faseDTO) {
        Campeonato campeonato = campeonatoRepository.findById(faseDTO.getCampeonatoId())
                .orElseThrow(() -> new NegocioException("Campeonato não encontrado"));

        Fase fase = new Fase();
        fase.setNome(faseDTO.getNome());
        fase.setDataInicio(faseDTO.getDataInicio());
        fase.setDataFim(faseDTO.getDataFim());
        fase.setNumeroTimes(faseDTO.getNumeroTimes());
        fase.setTipo(faseDTO.getTipo());
        fase.setCampeonato(campeonato);

        return toDTO(faseRepository.save(fase));
    }

    @Override
    @Transactional
    public FaseDTO atualizar(Long id, FaseDTO faseDTO) {
        Fase fase = buscarFaseEntity(id);
        
        fase.setNome(faseDTO.getNome());
        fase.setDataInicio(faseDTO.getDataInicio());
        fase.setDataFim(faseDTO.getDataFim());
        fase.setNumeroTimes(faseDTO.getNumeroTimes());
        fase.setTipo(faseDTO.getTipo());

        return toDTO(faseRepository.save(fase));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Fase fase = buscarFaseEntity(id);
        faseRepository.delete(fase);
    }

    @Override
    public FaseDTO buscarPorId(Long id) {
        return toDTO(buscarFaseEntity(id));
    }

    @Override
    public List<FaseDTO> listar() {
        return faseRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FaseDTO> listarPorCampeonato(Long campeonatoId) {
        Campeonato campeonato = campeonatoRepository.findById(campeonatoId)
                .orElseThrow(() -> new NegocioException("Campeonato não encontrado"));

        return faseRepository.findByCampeonato(campeonato).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void iniciarFase(Long id) {
        Fase fase = buscarFaseEntity(id);
        // Implementar lógica de início da fase
    }

    @Override
    @Transactional
    public void finalizarFase(Long id) {
        Fase fase = buscarFaseEntity(id);
        // Implementar lógica de finalização da fase
    }

    @Override
    @Transactional
    public FaseDTO criarFaseGrupos(Long campeonatoId) {
        Campeonato campeonato = campeonatoRepository.findById(campeonatoId)
                .orElseThrow(() -> new NegocioException("Campeonato não encontrado"));

        Fase fase = new Fase();
        fase.setNome("Fase de Grupos");
        fase.setTipo(TipoFase.GRUPOS);
        fase.setCampeonato(campeonato);
        fase.setNumeroTimes(campeonato.getQuantidadeMaximaTimes());
        // Definir datas com base no campeonato
        fase.setDataInicio(campeonato.getDataInicio());
        fase.setDataFim(campeonato.getDataFim());

        return toDTO(faseRepository.save(fase));
    }

    @Override
    @Transactional
    public FaseDTO criarProximaFase(Long campeonatoId) {
        // Implementar lógica de criação da próxima fase
        return null;
    }

    @Override
    @Transactional
    public void gerarPartidas(Long faseId) {
        Fase fase = buscarFaseEntity(faseId);
        // Implementar lógica de geração de partidas
    }

    private Fase buscarFaseEntity(Long id) {
        return faseRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Fase não encontrada"));
    }

    private FaseDTO toDTO(Fase fase) {
        FaseDTO dto = new FaseDTO();
        dto.setId(fase.getId());
        dto.setNome(fase.getNome());
        dto.setDataInicio(fase.getDataInicio());
        dto.setDataFim(fase.getDataFim());
        dto.setNumeroTimes(fase.getNumeroTimes());
        dto.setTipo(fase.getTipo());
        dto.setCampeonatoId(fase.getCampeonato().getId());
        return dto;
    }
} 