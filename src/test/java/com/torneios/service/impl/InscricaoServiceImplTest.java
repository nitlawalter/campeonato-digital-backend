package com.torneios.service.impl;

import com.torneios.model.Campeonato;
import com.torneios.model.Inscricao;
import com.torneios.model.Time;
import com.torneios.model.enums.StatusCampeonato;
import com.torneios.repository.InscricaoRepository;
import com.torneios.service.CampeonatoService;
import com.torneios.service.TimeService;
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
class InscricaoServiceImplTest {

    @Mock
    private InscricaoRepository inscricaoRepository;

    @Mock
    private CampeonatoService campeonatoService;

    @Mock
    private TimeService timeService;

    @InjectMocks
    private InscricaoServiceImpl inscricaoService;

    private Campeonato campeonato;
    private Time time;
    private Inscricao inscricao;

    @BeforeEach
    void setUp() {
        campeonato = new Campeonato();
        campeonato.setId(1L);
        campeonato.setNome("Campeonato Teste");
        campeonato.setStatus(StatusCampeonato.INSCRICOES_ABERTAS);
        campeonato.setNumeroMaximoTimes(16);

        time = new Time();
        time.setId(1L);
        time.setNome("Time Teste");
        time.setJogador("Jogador Teste");

        inscricao = new Inscricao();
        inscricao.setId(1L);
        inscricao.setCampeonato(campeonato);
        inscricao.setTime(time);
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setAprovada(false);
    }

    @Test
    void deveInscreverTimeComSucesso() {
        when(campeonatoService.buscarPorId(anyLong())).thenReturn(campeonato);
        when(timeService.buscarPorId(anyLong())).thenReturn(time);
        when(inscricaoRepository.findByCampeonatoAndTime(any(), any())).thenReturn(Optional.empty());
        when(inscricaoRepository.countByCampeonatoAndAprovada(any(), eq(true))).thenReturn(0L);
        when(inscricaoRepository.save(any(Inscricao.class))).thenReturn(inscricao);

        Inscricao resultado = inscricaoService.inscrever(1L, 1L);

        assertNotNull(resultado);
        assertFalse(resultado.getAprovada());
        assertEquals(time, resultado.getTime());
        assertEquals(campeonato, resultado.getCampeonato());
        verify(inscricaoRepository).save(any(Inscricao.class));
    }

    @Test
    void deveLancarExcecaoAoInscreverTimeJaInscrito() {
        when(campeonatoService.buscarPorId(anyLong())).thenReturn(campeonato);
        when(timeService.buscarPorId(anyLong())).thenReturn(time);
        when(inscricaoRepository.findByCampeonatoAndTime(any(), any())).thenReturn(Optional.of(inscricao));

        assertThrows(NegocioException.class, () -> {
            inscricaoService.inscrever(1L, 1L);
        });

        verify(inscricaoRepository, never()).save(any(Inscricao.class));
    }

    @Test
    void deveAprovarInscricaoComSucesso() {
        when(inscricaoRepository.findById(anyLong())).thenReturn(Optional.of(inscricao));
        when(inscricaoRepository.countByCampeonatoAndAprovada(any(), eq(true))).thenReturn(0L);
        when(inscricaoRepository.save(any(Inscricao.class))).thenReturn(inscricao);

        inscricaoService.aprovarInscricao(1L);

        assertTrue(inscricao.getAprovada());
        verify(inscricaoRepository).save(inscricao);
    }

    @Test
    void deveLancarExcecaoAoAprovarInscricaoJaAprovada() {
        inscricao.setAprovada(true);
        when(inscricaoRepository.findById(anyLong())).thenReturn(Optional.of(inscricao));

        assertThrows(NegocioException.class, () -> {
            inscricaoService.aprovarInscricao(1L);
        });

        verify(inscricaoRepository, never()).save(any(Inscricao.class));
    }

    @Test
    void deveListarInscricoesPorCampeonato() {
        List<Inscricao> inscricoes = Arrays.asList(inscricao);
        when(campeonatoService.buscarPorId(anyLong())).thenReturn(campeonato);
        when(inscricaoRepository.findByCampeonato(any(Campeonato.class))).thenReturn(inscricoes);

        List<Inscricao> resultado = inscricaoService.listarPorCampeonato(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(inscricaoRepository).findByCampeonato(campeonato);
    }

    @Test
    void deveListarInscricoesAprovadas() {
        inscricao.setAprovada(true);
        List<Inscricao> inscricoes = Arrays.asList(inscricao);
        when(campeonatoService.buscarPorId(anyLong())).thenReturn(campeonato);
        when(inscricaoRepository.findByCampeonatoAndAprovada(any(Campeonato.class), eq(true)))
                .thenReturn(inscricoes);

        List<Inscricao> resultado = inscricaoService.listarInscricoesAprovadas(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getAprovada());
        verify(inscricaoRepository).findByCampeonatoAndAprovada(campeonato, true);
    }
} 