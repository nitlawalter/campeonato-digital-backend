package com.torneios.service.impl;

import com.torneios.dto.CampeonatoDTO;
import com.torneios.exception.NegocioException;
import com.torneios.model.Campeonato;
import com.torneios.model.enums.StatusCampeonato;
import com.torneios.repository.CampeonatoRepository;
import com.torneios.repository.InscricaoRepository;
import com.torneios.service.CampeonatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampeonatoServiceImpl implements CampeonatoService {

    private final CampeonatoRepository campeonatoRepository;
    private final InscricaoRepository inscricaoRepository;

    @Override
    @Transactional
    public CampeonatoDTO criar(CampeonatoDTO dto) {
        validarCampeonato(dto);
        
        if (campeonatoRepository.existsByNome(dto.getNome())) {
            throw new NegocioException("Já existe um campeonato com este nome");
        }

        Campeonato campeonato = new Campeonato();
        campeonato.setNome(dto.getNome());
        campeonato.setDataInicio(dto.getDataInicio());
        campeonato.setDataFim(dto.getDataFim());
        campeonato.setQuantidadeMaximaTimes(dto.getQuantidadeMaximaTimes());
        campeonato.setStatus(StatusCampeonato.CRIADO);

        return toDTO(campeonatoRepository.save(campeonato));
    }

    @Override
    @Transactional
    public Campeonato atualizar(Long id, Campeonato campeonato) {
        Campeonato campeonatoExistente = buscarPorId(id);
        
        if (!campeonatoExistente.getStatus().equals(StatusCampeonato.CRIADO)) {
            throw new NegocioException("Não é possível alterar um campeonato que já foi iniciado");
        }

        validarCampeonato(toDTO(campeonato));
        
        campeonatoExistente.setNome(campeonato.getNome());
        campeonatoExistente.setDataInicio(campeonato.getDataInicio());
        campeonatoExistente.setDataFim(campeonato.getDataFim());
        campeonatoExistente.setQuantidadeMaximaTimes(campeonato.getQuantidadeMaximaTimes());

        return campeonatoRepository.save(campeonatoExistente);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Campeonato campeonato = buscarPorId(id);
        
        if (!campeonato.getStatus().equals(StatusCampeonato.CRIADO)) {
            throw new NegocioException("Não é possível excluir um campeonato que já foi iniciado");
        }

        campeonatoRepository.delete(campeonato);
    }

    @Override
    public Campeonato buscarPorId(Long id) {
        return campeonatoRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Campeonato não encontrado"));
    }

    @Override
    public List<Campeonato> listarTodos() {
        return campeonatoRepository.findAll();
    }

    @Override
    @Transactional
    public void iniciarInscricoes(Long id) {
        Campeonato campeonato = buscarPorId(id);
        
        if (!campeonato.getStatus().equals(StatusCampeonato.CRIADO)) {
            throw new NegocioException("Campeonato não está no status correto para iniciar inscrições");
        }

        campeonato.setStatus(StatusCampeonato.INSCRICOES_ABERTAS);
        campeonatoRepository.save(campeonato);
    }

    @Override
    @Transactional
    public void encerrarInscricoes(Long id) {
        Campeonato campeonato = buscarPorId(id);
        
        if (!campeonato.getStatus().equals(StatusCampeonato.INSCRICOES_ABERTAS)) {
            throw new NegocioException("Campeonato não está com inscrições abertas");
        }

        long inscricoesAprovadas = inscricaoRepository.countByCampeonatoAndAprovada(campeonato, true);
        if (inscricoesAprovadas < 2) { // Mínimo de 2 times para um campeonato
            throw new NegocioException("Número mínimo de times não foi atingido");
        }

        campeonato.setStatus(StatusCampeonato.INSCRICOES_ENCERRADAS);
        campeonatoRepository.save(campeonato);
    }

    @Override
    @Transactional
    public void iniciarCampeonato(Long id) {
        Campeonato campeonato = buscarPorId(id);
        
        if (!campeonato.getStatus().equals(StatusCampeonato.INSCRICOES_ENCERRADAS)) {
            throw new NegocioException("Campeonato não está pronto para ser iniciado");
        }

        campeonato.setStatus(StatusCampeonato.EM_ANDAMENTO);
        campeonatoRepository.save(campeonato);
    }

    @Override
    @Transactional
    public void finalizarCampeonato(Long id) {
        Campeonato campeonato = buscarPorId(id);
        
        if (!campeonato.getStatus().equals(StatusCampeonato.EM_ANDAMENTO)) {
            throw new NegocioException("Campeonato não está em andamento");
        }

        campeonato.setStatus(StatusCampeonato.FINALIZADO);
        campeonatoRepository.save(campeonato);
    }

    private void validarCampeonato(CampeonatoDTO dto) {
        if (dto.getDataInicio().isBefore(LocalDate.now())) {
            throw new NegocioException("Data de início não pode ser anterior à data atual");
        }
        
        if (dto.getDataFim().isBefore(dto.getDataInicio())) {
            throw new NegocioException("Data de fim não pode ser anterior à data de início");
        }
        
        if (dto.getQuantidadeMaximaTimes() < 2) {
            throw new NegocioException("Quantidade máxima de times deve ser maior que 1");
        }
    }

    private CampeonatoDTO toDTO(Campeonato campeonato) {
        CampeonatoDTO dto = new CampeonatoDTO();
        dto.setId(campeonato.getId());
        dto.setNome(campeonato.getNome());
        dto.setDataInicio(campeonato.getDataInicio());
        dto.setDataFim(campeonato.getDataFim());
        dto.setQuantidadeMaximaTimes(campeonato.getQuantidadeMaximaTimes());
        dto.setStatus(campeonato.getStatus());
        return dto;
    }
} 