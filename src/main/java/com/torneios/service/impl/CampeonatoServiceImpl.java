package com.torneios.service.impl;

import com.torneios.model.Campeonato;
import com.torneios.model.enums.StatusCampeonato;
import com.torneios.repository.CampeonatoRepository;
import com.torneios.repository.InscricaoRepository;
import com.torneios.service.CampeonatoService;
import com.torneios.exception.NegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampeonatoServiceImpl implements CampeonatoService {

    private final CampeonatoRepository campeonatoRepository;
    private final InscricaoRepository inscricaoRepository;

    @Override
    @Transactional
    public Campeonato criar(Campeonato campeonato) {
        validarCampeonato(campeonato);
        
        if (campeonatoRepository.existsByNome(campeonato.getNome())) {
            throw new NegocioException("Já existe um campeonato com este nome");
        }

        campeonato.setStatus(StatusCampeonato.CRIADO);
        return campeonatoRepository.save(campeonato);
    }

    @Override
    @Transactional
    public Campeonato atualizar(Long id, Campeonato campeonato) {
        Campeonato campeonatoExistente = buscarPorId(id);
        
        if (!campeonatoExistente.getStatus().equals(StatusCampeonato.CRIADO)) {
            throw new NegocioException("Não é possível alterar um campeonato que já foi iniciado");
        }

        validarCampeonato(campeonato);
        
        campeonatoExistente.setNome(campeonato.getNome());
        campeonatoExistente.setDataInicio(campeonato.getDataInicio());
        campeonatoExistente.setDataFim(campeonato.getDataFim());
        campeonatoExistente.setNumeroGrupos(campeonato.getNumeroGrupos());
        campeonatoExistente.setTimesPorGrupo(campeonato.getTimesPorGrupo());
        campeonatoExistente.setNumeroMaximoTimes(campeonato.getNumeroMaximoTimes());

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
        if (inscricoesAprovadas < campeonato.getNumeroGrupos() * campeonato.getTimesPorGrupo()) {
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

    private void validarCampeonato(Campeonato campeonato) {
        if (campeonato.getDataInicio().isBefore(LocalDateTime.now())) {
            throw new NegocioException("Data de início não pode ser anterior à data atual");
        }
        
        if (campeonato.getDataFim().isBefore(campeonato.getDataInicio())) {
            throw new NegocioException("Data de fim não pode ser anterior à data de início");
        }
        
        if (campeonato.getNumeroGrupos() <= 0) {
            throw new NegocioException("Número de grupos deve ser maior que zero");
        }
        
        if (campeonato.getTimesPorGrupo() <= 1) {
            throw new NegocioException("Número de times por grupo deve ser maior que um");
        }
        
        if (campeonato.getNumeroMaximoTimes() < (campeonato.getNumeroGrupos() * campeonato.getTimesPorGrupo())) {
            throw new NegocioException("Número máximo de times deve ser maior ou igual ao número de times necessários");
        }
    }
} 