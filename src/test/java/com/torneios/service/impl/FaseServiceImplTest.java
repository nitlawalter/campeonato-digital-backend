package com.torneios.service.impl;

import com.torneios.model.*;
import com.torneios.model.enums.StatusCampeonato;
import com.torneios.model.enums.StatusPartida;
import com.torneios.model.enums.TipoFase;
import com.torneios.repository.FaseRepository;
import com.torneios.repository.PartidaRepository;
import com.torneios.service.CampeonatoService;
import com.torneios.service.InscricaoService;
import com.torneios.exception.NegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FaseServiceImplTest {

    @Mock
    private FaseRepository faseRepository;

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private CampeonatoService campeonatoService;

    @Mock
    private InscricaoService inscricaoService;

    @InjectMocks
    private FaseServiceImpl faseService;

    private Campeonato campeonato;
    private Fase fase;
    private List<Time> times;

    @BeforeEach
    void setUp() {
        campeonato = new Campeonato();
        campeonato.setId(1L);
        campeonato.setNumeroGrupos(2);
        campeonato.setTimesPorGrupo(4);
        campeonato.setStatus(StatusCampeonato.INSCRICOES_ENCERRADAS);

        fase = new Fase();
        fase.setId(1L);
        fase.setCampeonato(campeonato);
        fase.setTipo(TipoFase.GRUPOS);
        fase.setNumero(1);

        times = Arrays.asList(
            createTime(1L, "Time 1"),
            createTime(2L, "Time 2"),
            createTime(3L, "Time 3"),
            createTime(4L, "Time 4")
        );
    }

    @Test
    void deveCriarFaseGruposComSucesso() {
        when(campeonatoService.buscarPorId(anyLong())).thenReturn(campeonato);
        when(faseRepository.findByCampeonato(any())).thenReturn(Collections.emptyList());
        when(faseRepository.save(any(Fase.class))).thenReturn(fase);

        Fase resultado = faseService.criarFaseGrupos(1L);

        assertNotNull(resultado);
        assertEquals(TipoFase.GRUPOS, resultado.getTipo());
        assertEquals(1, resultado.getNumero());
        verify(faseRepository).save(any(Fase.class));
    }

    @Test
    void deveLancarExcecaoAoCriarFaseGruposQuandoJaExistemFases() {
        when(campeonatoService.buscarPorId(anyLong())).thenReturn(campeonato);
        when(faseRepository.findByCampeonato(any())).thenReturn(Arrays.asList(fase));

        assertThrows(NegocioException.class, () -> {
            faseService.criarFaseGrupos(1L);
        });

        verify(faseRepository, never()).save(any(Fase.class));
    }

    @Test
    void deveCriarProximaFaseComSucesso() {
        Fase faseAnterior = fase;
        Fase novaFase = new Fase();
        novaFase.setTipo(TipoFase.OITAVAS);
        novaFase.setNumero(2);

        when(campeonatoService.buscarPorId(anyLong())).thenReturn(campeonato);
        when(faseRepository.findByCampeonatoOrderByNumeroAsc(any())).thenReturn(Arrays.asList(faseAnterior));
        when(partidaRepository.findByFase(any())).thenReturn(createPartidasFinalizadas());
        when(faseRepository.save(any(Fase.class))).thenReturn(novaFase);

        Fase resultado = faseService.criarProximaFase(1L);

        assertNotNull(resultado);
        assertEquals(TipoFase.OITAVAS, resultado.getTipo());
        assertEquals(2, resultado.getNumero());
        verify(faseRepository).save(any(Fase.class));
    }

    @Test
    void deveGerarPartidasComSucesso() {
        when(faseRepository.findById(anyLong())).thenReturn(Optional.of(fase));
        when(partidaRepository.findByFase(any())).thenReturn(Collections.emptyList());
        when(inscricaoService.listarInscricoesAprovadas(anyLong())).thenReturn(createInscricoes());

        faseService.gerarPartidas(1L);

        verify(partidaRepository, atLeastOnce()).save(any(Partida.class));
    }

    private Time createTime(Long id, String nome) {
        Time time = new Time();
        time.setId(id);
        time.setNome(nome);
        return time;
    }

    private List<Partida> createPartidasFinalizadas() {
        Partida partida = new Partida();
        partida.setStatus(StatusPartida.FINALIZADA);
        return Arrays.asList(partida);
    }

    private List<Inscricao> createInscricoes() {
        return times.stream()
                .map(time -> {
                    Inscricao inscricao = new Inscricao();
                    inscricao.setTime(time);
                    inscricao.setAprovada(true);
                    return inscricao;
                })
                .collect(Collectors.toList());
    }
} 