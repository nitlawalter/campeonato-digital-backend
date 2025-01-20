package com.torneios.service.impl;

import com.torneios.dto.InscricaoDTO;
import com.torneios.dto.TimeDTO;
import com.torneios.exception.NegocioException;
import com.torneios.model.Campeonato;
import com.torneios.model.Inscricao;
import com.torneios.model.Time;
import com.torneios.model.enums.StatusCampeonato;
import com.torneios.repository.CampeonatoRepository;
import com.torneios.repository.InscricaoRepository;
import com.torneios.repository.TimeRepository;
import com.torneios.service.InscricaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscricaoServiceImpl implements InscricaoService {

    private final InscricaoRepository inscricaoRepository;
    private final TimeRepository timeRepository;
    private final CampeonatoRepository campeonatoRepository;

    @Override
    @Transactional
    public InscricaoDTO inscrever(Long campeonatoId, Long timeId) {
        Time time = timeRepository.findById(timeId)
                .orElseThrow(() -> new NegocioException("Time não encontrado"));

        Campeonato campeonato = campeonatoRepository.findById(campeonatoId)
                .orElseThrow(() -> new NegocioException("Campeonato não encontrado"));

        if (!campeonato.getStatus().equals(StatusCampeonato.INSCRICOES_ABERTAS)) {
            throw new NegocioException("Campeonato não está com inscrições abertas");
        }

        if (inscricaoRepository.existsByTimeAndCampeonato(time, campeonato)) {
            throw new NegocioException("Time já está inscrito neste campeonato");
        }

        if (inscricaoRepository.countByCampeonatoAndAprovada(campeonato, true) >= campeonato.getQuantidadeMaximaTimes()) {
            throw new NegocioException("Campeonato já atingiu o número máximo de times");
        }

        Inscricao inscricao = new Inscricao();
        inscricao.setTime(time);
        inscricao.setCampeonato(campeonato);
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setAprovada(false);

        inscricao = inscricaoRepository.save(inscricao);
        return toDTO(inscricao);
    }

    @Override
    @Transactional
    public void aprovarInscricao(Long inscricaoId) {
        Inscricao inscricao = buscarInscricao(inscricaoId);
        
        if (inscricao.isAprovada()) {
            throw new NegocioException("Inscrição já está aprovada");
        }

        if (!inscricao.getCampeonato().getStatus().equals(StatusCampeonato.INSCRICOES_ABERTAS)) {
            throw new NegocioException("Campeonato não está com inscrições abertas");
        }

        if (inscricaoRepository.countByCampeonatoAndAprovada(inscricao.getCampeonato(), true) >= 
            inscricao.getCampeonato().getQuantidadeMaximaTimes()) {
            throw new NegocioException("Número máximo de times já foi atingido");
        }

        inscricao.setAprovada(true);
        inscricaoRepository.save(inscricao);
    }

    @Override
    @Transactional
    public void reprovarInscricao(Long inscricaoId) {
        Inscricao inscricao = buscarInscricao(inscricaoId);
        
        if (inscricao.isAprovada()) {
            throw new NegocioException("Não é possível reprovar uma inscrição já aprovada");
        }

        inscricaoRepository.delete(inscricao);
    }

    @Override
    public List<InscricaoDTO> listarPorCampeonato(Long campeonatoId) {
        Campeonato campeonato = campeonatoRepository.findById(campeonatoId)
                .orElseThrow(() -> new NegocioException("Campeonato não encontrado"));

        return inscricaoRepository.findByCampeonato(campeonato).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InscricaoDTO> listarInscricoesAprovadas(Long campeonatoId) {
        Campeonato campeonato = campeonatoRepository.findById(campeonatoId)
                .orElseThrow(() -> new NegocioException("Campeonato não encontrado"));

        return inscricaoRepository.findByCampeonatoAndAprovada(campeonato, true).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private Inscricao buscarInscricao(Long id) {
        return inscricaoRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Inscrição não encontrada"));
    }

    private InscricaoDTO toDTO(Inscricao inscricao) {
        InscricaoDTO dto = new InscricaoDTO();
        dto.setId(inscricao.getId());
        dto.setTimeId(inscricao.getTime().getId());
        dto.setCampeonatoId(inscricao.getCampeonato().getId());
        dto.setDataInscricao(inscricao.getDataInscricao());
        dto.setAprovada(inscricao.isAprovada());
        
        TimeDTO timeDTO = new TimeDTO();
        timeDTO.setId(inscricao.getTime().getId());
        timeDTO.setNome(inscricao.getTime().getNome());
        timeDTO.setAbreviacao(inscricao.getTime().getAbreviacao());
        timeDTO.setCidade(inscricao.getTime().getCidade());
        timeDTO.setEstado(inscricao.getTime().getEstado());
        timeDTO.setLogo(inscricao.getTime().getLogo());
        
        dto.setTime(timeDTO);
        return dto;
    }
} 