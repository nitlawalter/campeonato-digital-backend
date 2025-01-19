package com.torneios.service.impl;

import com.torneios.model.Partida;
import com.torneios.model.Fase;
import com.torneios.model.Time;
import com.torneios.model.enums.StatusPartida;
import com.torneios.repository.PartidaRepository;
import com.torneios.repository.FaseRepository;
import com.torneios.service.NotificacaoService;
import com.torneios.exception.NegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartidaServiceImplTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private FaseRepository faseRepository;

    @Mock
    private NotificacaoService notificacaoService;

    @InjectMocks
    private PartidaServiceImpl partidaService;

    private Partida partida;
    private Fase fase;
    private Time timeCasa;
    private Time timeVisitante;

    @BeforeEach
    void setUp() {
        timeCasa = new Time();
        timeCasa.setId(1L);
        timeCasa.setNome("Time Casa");

        timeVisitante = new Time();
        timeVisitante.setId(2L);
        timeVisitante.setNome("Time Visitante");

        fase = new Fase();
        fase.setId(1L);

        partida = new Partida();
        partida.setId(1L);
        partida.setFase(fase);
        partida.setTimeCasa(timeCasa);
        partida.setTimeVisitante(timeVisitante);
        partida.setDataHora(LocalDateTime.now());
        partida.setStatus(StatusPartida.AGENDADA);
    }

    @Test
    void deveRegistrarResultadoComSucesso() {
        when(partidaRepository.findById(anyLong())).thenReturn(Optional.of(partida));
        when(partidaRepository.save(any(Partida.class))).thenReturn(partida);
        doNothing().when(notificacaoService).notificarResultadoPartida(any(Partida.class));

        Partida resultado = partidaService.registrarResultado(1L, 2, 1);

        assertNotNull(resultado);
        assertEquals(StatusPartida.FINALIZADA, resultado.getStatus());
        assertEquals(2, resultado.getGolsTimeCasa());
        assertEquals(1, resultado.getGolsTimeVisitante());
        verify(notificacaoService).notificarResultadoPartida(resultado);
    }

    @Test
    void deveLancarExcecaoAoRegistrarResultadoDePartidaFinalizada() {
        partida.setStatus(StatusPartida.FINALIZADA);
        when(partidaRepository.findById(anyLong())).thenReturn(Optional.of(partida));

        assertThrows(NegocioException.class, () -> {
            partidaService.registrarResultado(1L, 2, 1);
        });

        verify(partidaRepository, never()).save(any(Partida.class));
        verify(notificacaoService, never()).notificarResultadoPartida(any(Partida.class));
    }

    @Test
    void deveLancarExcecaoAoRegistrarResultadoComGolsNegativos() {
        when(partidaRepository.findById(anyLong())).thenReturn(Optional.of(partida));

        assertThrows(NegocioException.class, () -> {
            partidaService.registrarResultado(1L, -1, 1);
        });

        verify(partidaRepository, never()).save(any(Partida.class));
        verify(notificacaoService, never()).notificarResultadoPartida(any(Partida.class));
    }

    @Test
    void deveListarPartidasPorFase() {
        List<Partida> partidas = Arrays.asList(partida);
        when(faseRepository.findById(anyLong())).thenReturn(Optional.of(fase));
        when(partidaRepository.findByFase(any(Fase.class))).thenReturn(partidas);

        List<Partida> resultado = partidaService.listarPorFase(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(partidaRepository).findByFase(fase);
    }

    @Test
    void deveLancarExcecaoAoListarPartidasDeFaseInexistente() {
        when(faseRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NegocioException.class, () -> {
            partidaService.listarPorFase(1L);
        });

        verify(partidaRepository, never()).findByFase(any(Fase.class));
    }
} 