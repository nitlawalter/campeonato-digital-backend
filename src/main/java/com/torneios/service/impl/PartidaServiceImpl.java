package com.torneios.service.impl;

import com.torneios.model.Partida;
import com.torneios.model.Fase;
import com.torneios.model.enums.StatusPartida;
import com.torneios.repository.PartidaRepository;
import com.torneios.repository.FaseRepository;
import com.torneios.service.NotificacaoService;
import com.torneios.service.PartidaService;
import com.torneios.exception.NegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartidaServiceImpl implements PartidaService {

    private final PartidaRepository partidaRepository;
    private final FaseRepository faseRepository;
    private final NotificacaoService notificacaoService;

    @Override
    @Transactional
    public Partida registrarResultado(Long partidaId, Integer golsTimeCasa, Integer golsTimeVisitante) {
        Partida partida = buscarPorId(partidaId);
        
        if (partida.getStatus().equals(StatusPartida.FINALIZADA)) {
            throw new NegocioException("Não é possível alterar o resultado de uma partida finalizada");
        }
        
        if (golsTimeCasa < 0 || golsTimeVisitante < 0) {
            throw new NegocioException("O número de gols não pode ser negativo");
        }

        partida.setGolsTimeCasa(golsTimeCasa);
        partida.setGolsTimeVisitante(golsTimeVisitante);
        partida.setStatus(StatusPartida.FINALIZADA);

        Partida partidaSalva = partidaRepository.save(partida);
        notificacaoService.notificarResultadoPartida(partidaSalva);
        
        return partidaSalva;
    }

    @Override
    public List<Partida> listarPorFase(Long faseId) {
        Fase fase = faseRepository.findById(faseId)
                .orElseThrow(() -> new NegocioException("Fase não encontrada"));
                
        return partidaRepository.findByFase(fase);
    }

    @Override
    public Partida buscarPorId(Long id) {
        return partidaRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Partida não encontrada"));
    }
} 