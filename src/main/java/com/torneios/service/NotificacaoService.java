package com.torneios.service;

import com.torneios.model.Partida;
import com.torneios.model.Campeonato;
import com.torneios.model.Time;
import com.torneios.model.enums.TipoFase;

public interface NotificacaoService {
    void notificarResultadoPartida(Partida partida);

    void notificarInicioFase(Campeonato campeonato, TipoFase tipoFase);

    void notificarInscricaoAprovada(Campeonato campeonato, Time time);
} 