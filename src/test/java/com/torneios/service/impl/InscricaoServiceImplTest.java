package com.torneios.service.impl;

import com.torneios.dto.InscricaoDTO;
import com.torneios.exception.NegocioException;
import com.torneios.model.Campeonato;
import com.torneios.model.Inscricao;
import com.torneios.model.Time;
import com.torneios.model.enums.StatusCampeonato;
import com.torneios.repository.CampeonatoRepository;
import com.torneios.repository.InscricaoRepository;
import com.torneios.repository.TimeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
    private CampeonatoRepository campeonatoRepository;

    @Mock
    private TimeRepository timeRepository;

    @InjectMocks
    private InscricaoServiceImpl inscricaoService;

    @Test
    void inscrever_DeveRetornarInscricaoCriada() {
        // Arrange
        Long campeonatoId = 1L;
        Long timeId = 1L;

        Campeonato campeonato = new Campeonato();
        campeonato.setId(campeonatoId);
        campeonato.setStatus(StatusCampeonato.INSCRICOES_ABERTAS);
        campeonato.setQuantidadeMaximaTimes(16);

        Time time = new Time();
        time.setId(timeId);
        time.setNome("Time Teste");

        when(campeonatoRepository.findById(campeonatoId)).thenReturn(Optional.of(campeonato));
        when(timeRepository.findById(timeId)).thenReturn(Optional.of(time));
        when(inscricaoRepository.existsByTimeAndCampeonato(time, campeonato)).thenReturn(false);
        when(inscricaoRepository.countByCampeonatoAndAprovada(campeonato, true)).thenReturn(0L);
        when(inscricaoRepository.save(any(Inscricao.class))).thenAnswer(i -> {
            Inscricao inscricao = i.getArgument(0);
            inscricao.setId(1L);
            return inscricao;
        });

        // Act
        InscricaoDTO result = inscricaoService.inscrever(campeonatoId, timeId);

        // Assert
        assertNotNull(result);
        assertEquals(timeId, result.getTimeId());
        assertEquals(campeonatoId, result.getCampeonatoId());
        assertFalse(result.isAprovada());
    }

    @Test
    void inscrever_DeveRetornarErroQuandoCampeonatoNaoExiste() {
        // Arrange
        when(campeonatoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NegocioException.class, () -> inscricaoService.inscrever(1L, 1L));
    }

    @Test
    void aprovarInscricao_DeveAprovarInscricao() {
        // Arrange
        Inscricao inscricao = new Inscricao();
        inscricao.setId(1L);
        inscricao.setAprovada(false);
        inscricao.setCampeonato(new Campeonato());
        inscricao.setTime(new Time());

        when(inscricaoRepository.findById(1L)).thenReturn(Optional.of(inscricao));
        when(inscricaoRepository.save(any(Inscricao.class))).thenReturn(inscricao);

        // Act
        inscricaoService.aprovarInscricao(1L);

        // Assert
        verify(inscricaoRepository).save(inscricao);
        assertTrue(inscricao.isAprovada());
    }

    @Test
    void listarPorCampeonato_DeveRetornarListaDeInscricoes() {
        // Arrange
        Campeonato campeonato = new Campeonato();
        campeonato.setId(1L);

        Time time = new Time();
        time.setId(1L);
        time.setNome("Time Teste");

        Inscricao inscricao = new Inscricao();
        inscricao.setId(1L);
        inscricao.setCampeonato(campeonato);
        inscricao.setTime(time);
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setAprovada(false);

        when(campeonatoRepository.findById(1L)).thenReturn(Optional.of(campeonato));
        when(inscricaoRepository.findByCampeonato(campeonato)).thenReturn(Arrays.asList(inscricao));

        // Act
        List<InscricaoDTO> result = inscricaoService.listarPorCampeonato(1L);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(time.getId(), result.get(0).getTimeId());
    }
} 