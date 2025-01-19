package com.torneios.service.impl;

import com.torneios.model.Campeonato;
import com.torneios.model.enums.StatusCampeonato;
import com.torneios.repository.CampeonatoRepository;
import com.torneios.repository.InscricaoRepository;
import com.torneios.exception.NegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampeonatoServiceImplTest {

    @Mock
    private CampeonatoRepository campeonatoRepository;

    @Mock
    private InscricaoRepository inscricaoRepository;

    @InjectMocks
    private CampeonatoServiceImpl campeonatoService;

    private Campeonato campeonato;

    @BeforeEach
    void setUp() {
        campeonato = new Campeonato();
        campeonato.setId(1L);
        campeonato.setNome("Campeonato Teste");
        campeonato.setDataInicio(LocalDateTime.now().plusDays(1));
        campeonato.setDataFim(LocalDateTime.now().plusDays(30));
        campeonato.setNumeroGrupos(4);
        campeonato.setTimesPorGrupo(4);
        campeonato.setNumeroMaximoTimes(16);
        campeonato.setStatus(StatusCampeonato.CRIADO);
    }

    @Test
    void deveCriarCampeonatoComSucesso() {
        when(campeonatoRepository.existsByNome(anyString())).thenReturn(false);
        when(campeonatoRepository.save(any(Campeonato.class))).thenReturn(campeonato);

        Campeonato resultado = campeonatoService.criar(campeonato);

        assertNotNull(resultado);
        assertEquals(StatusCampeonato.CRIADO, resultado.getStatus());
        verify(campeonatoRepository).save(any(Campeonato.class));
    }

    @Test
    void deveLancarExcecaoAoCriarCampeonatoComNomeExistente() {
        when(campeonatoRepository.existsByNome(anyString())).thenReturn(true);

        assertThrows(NegocioException.class, () -> {
            campeonatoService.criar(campeonato);
        });

        verify(campeonatoRepository, never()).save(any(Campeonato.class));
    }

    @Test
    void deveIniciarInscricoesComSucesso() {
        when(campeonatoRepository.findById(anyLong())).thenReturn(Optional.of(campeonato));
        when(campeonatoRepository.save(any(Campeonato.class))).thenReturn(campeonato);

        campeonatoService.iniciarInscricoes(1L);

        assertEquals(StatusCampeonato.INSCRICOES_ABERTAS, campeonato.getStatus());
        verify(campeonatoRepository).save(campeonato);
    }

    @Test
    void deveLancarExcecaoAoIniciarInscricoesComStatusInvalido() {
        campeonato.setStatus(StatusCampeonato.INSCRICOES_ABERTAS);
        when(campeonatoRepository.findById(anyLong())).thenReturn(Optional.of(campeonato));

        assertThrows(NegocioException.class, () -> {
            campeonatoService.iniciarInscricoes(1L);
        });

        verify(campeonatoRepository, never()).save(any(Campeonato.class));
    }

    @Test
    void deveEncerrarInscricoesComSucesso() {
        campeonato.setStatus(StatusCampeonato.INSCRICOES_ABERTAS);
        when(campeonatoRepository.findById(anyLong())).thenReturn(Optional.of(campeonato));
        when(inscricaoRepository.countByCampeonatoAndAprovada(any(), eq(true)))
                .thenReturn((long) (campeonato.getNumeroGrupos() * campeonato.getTimesPorGrupo()));

        campeonatoService.encerrarInscricoes(1L);

        assertEquals(StatusCampeonato.INSCRICOES_ENCERRADAS, campeonato.getStatus());
        verify(campeonatoRepository).save(campeonato);
    }

    @Test
    void deveLancarExcecaoAoEncerrarInscricoesComTimesInsuficientes() {
        campeonato.setStatus(StatusCampeonato.INSCRICOES_ABERTAS);
        when(campeonatoRepository.findById(anyLong())).thenReturn(Optional.of(campeonato));
        when(inscricaoRepository.countByCampeonatoAndAprovada(any(), eq(true)))
                .thenReturn(1L);

        assertThrows(NegocioException.class, () -> {
            campeonatoService.encerrarInscricoes(1L);
        });

        verify(campeonatoRepository, never()).save(any(Campeonato.class));
    }
} 