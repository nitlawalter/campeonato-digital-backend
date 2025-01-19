package com.torneios.service.impl;

import com.torneios.model.Partida;
import com.torneios.model.Campeonato;
import com.torneios.model.Time;
import com.torneios.model.enums.TipoFase;
import com.torneios.service.NotificacaoService;
import com.torneios.dto.NotificacaoFaseDTO;
import com.torneios.dto.NotificacaoInscricaoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacaoServiceImpl implements NotificacaoService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void notificarResultadoPartida(Partida partida) {
        String destination = String.format("/topic/campeonatos/%d/partidas/%d", 
            partida.getFase().getCampeonato().getId(), 
            partida.getId());
            
        messagingTemplate.convertAndSend(destination, partida);
    }

    @Override
    public void notificarInicioFase(Campeonato campeonato, TipoFase tipoFase) {
        String mensagem = String.format("Iniciada a fase %s do campeonato %s", 
                tipoFase.name(), 
                campeonato.getNome());
        
        NotificacaoFaseDTO notificacao = new NotificacaoFaseDTO(
            campeonato.getId(),
            campeonato.getNome(),
            tipoFase.name(),
            mensagem
        );
        
        messagingTemplate.convertAndSend("/topic/fases", notificacao);
    }

    @Override
    public void notificarInscricaoAprovada(Campeonato campeonato, Time time) {
        String mensagem = String.format("Time %s aprovado para participar do campeonato %s", 
                time.getNome(), 
                campeonato.getNome());
        
        NotificacaoInscricaoDTO notificacao = new NotificacaoInscricaoDTO(
            campeonato.getId(),
            time.getId(),
            campeonato.getNome(),
            time.getNome(),
            mensagem
        );
        
        messagingTemplate.convertAndSend("/topic/inscricoes", notificacao);
    }
} 