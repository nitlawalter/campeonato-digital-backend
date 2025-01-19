package com.torneios.service.impl;

import com.torneios.model.Time;
import com.torneios.repository.TimeRepository;
import com.torneios.exception.NegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeServiceImplTest {

    @Mock
    private TimeRepository timeRepository;

    @InjectMocks
    private TimeServiceImpl timeService;

    private Time time;

    @BeforeEach
    void setUp() {
        time = new Time();
        time.setId(1L);
        time.setNome("Time Teste");
        time.setJogador("Jogador Teste");
        time.setEmblema("url-emblema");
    }

    @Test
    void deveCriarTimeComSucesso() {
        when(timeRepository.existsByNomeAndJogador(anyString(), anyString())).thenReturn(false);
        when(timeRepository.save(any(Time.class))).thenReturn(time);

        Time resultado = timeService.criar(time);

        assertNotNull(resultado);
        assertEquals("Time Teste", resultado.getNome());
        assertEquals("Jogador Teste", resultado.getJogador());
        verify(timeRepository).save(any(Time.class));
    }

    @Test
    void deveLancarExcecaoAoCriarTimeComNomeExistente() {
        when(timeRepository.existsByNomeAndJogador(anyString(), anyString())).thenReturn(true);

        assertThrows(NegocioException.class, () -> {
            timeService.criar(time);
        });

        verify(timeRepository, never()).save(any(Time.class));
    }

    @Test
    void deveAtualizarTimeComSucesso() {
        when(timeRepository.findById(anyLong())).thenReturn(Optional.of(time));
        when(timeRepository.save(any(Time.class))).thenReturn(time);

        Time timeAtualizado = new Time();
        timeAtualizado.setNome("Time Atualizado");
        timeAtualizado.setJogador("Jogador Teste");
        timeAtualizado.setEmblema("nova-url-emblema");

        Time resultado = timeService.atualizar(1L, timeAtualizado);

        assertNotNull(resultado);
        assertEquals("Time Atualizado", resultado.getNome());
        assertEquals("nova-url-emblema", resultado.getEmblema());
        verify(timeRepository).save(any(Time.class));
    }

    @Test
    void deveListarTodosOsTimes() {
        List<Time> times = Arrays.asList(
            time,
            new Time() {{
                setId(2L);
                setNome("Time 2");
                setJogador("Jogador 2");
            }}
        );

        when(timeRepository.findAll()).thenReturn(times);

        List<Time> resultado = timeService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(timeRepository).findAll();
    }

    @Test
    void deveDeletarTimeComSucesso() {
        when(timeRepository.findById(anyLong())).thenReturn(Optional.of(time));
        doNothing().when(timeRepository).delete(any(Time.class));

        timeService.deletar(1L);

        verify(timeRepository).delete(time);
    }

    @Test
    void deveLancarExcecaoAoDeletarTimeInexistente() {
        when(timeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NegocioException.class, () -> {
            timeService.deletar(1L);
        });

        verify(timeRepository, never()).delete(any(Time.class));
    }
} 