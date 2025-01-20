package com.torneios.service.impl;

import com.torneios.dto.TimeDTO;
import com.torneios.exception.NegocioException;
import com.torneios.model.Time;
import com.torneios.repository.TimeRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeServiceImplTest {

    @Mock
    private TimeRepository timeRepository;

    @InjectMocks
    private TimeServiceImpl timeService;

    @Test
    void criar_DeveRetornarTimeCriado() {
        // Arrange
        TimeDTO dto = criarTimeDTO();
        when(timeRepository.existsByNome(dto.getNome())).thenReturn(false);
        when(timeRepository.save(any(Time.class))).thenAnswer(i -> {
            Time time = i.getArgument(0);
            time.setId(1L);
            return time;
        });

        // Act
        TimeDTO result = timeService.criar(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getNome(), result.getNome());
        assertEquals(dto.getAbreviacao(), result.getAbreviacao());
        assertEquals(dto.getCidade(), result.getCidade());
        assertEquals(dto.getEstado(), result.getEstado());
    }

    @Test
    void criar_DeveRetornarErroQuandoTimeJaExiste() {
        // Arrange
        TimeDTO dto = criarTimeDTO();
        when(timeRepository.existsByNome(dto.getNome())).thenReturn(true);

        // Act & Assert
        assertThrows(NegocioException.class, () -> timeService.criar(dto));
        verify(timeRepository, never()).save(any(Time.class));
    }

    @Test
    void atualizar_DeveRetornarTimeAtualizado() {
        // Arrange
        Long id = 1L;
        TimeDTO dto = criarTimeDTO();
        Time timeExistente = criarTime();
        
        when(timeRepository.findById(id)).thenReturn(Optional.of(timeExistente));
        when(timeRepository.existsByNomeAndIdNot(dto.getNome(), id)).thenReturn(false);
        when(timeRepository.save(any(Time.class))).thenReturn(timeExistente);

        // Act
        TimeDTO result = timeService.atualizar(id, dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getNome(), result.getNome());
        assertEquals(dto.getAbreviacao(), result.getAbreviacao());
    }

    @Test
    void buscarPorId_DeveRetornarTime() {
        // Arrange
        Time time = criarTime();
        when(timeRepository.findById(1L)).thenReturn(Optional.of(time));

        // Act
        TimeDTO result = timeService.buscarPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(time.getNome(), result.getNome());
    }

    @Test
    void buscarPorId_DeveRetornarErroQuandoTimeNaoExiste() {
        // Arrange
        when(timeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NegocioException.class, () -> timeService.buscarPorId(1L));
    }

    @Test
    void listar_DeveRetornarListaDeTimes() {
        // Arrange
        List<Time> times = Arrays.asList(criarTime(), criarTime());
        when(timeRepository.findAll()).thenReturn(times);

        // Act
        List<TimeDTO> result = timeService.listar();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    private TimeDTO criarTimeDTO() {
        TimeDTO dto = new TimeDTO();
        dto.setNome("Time Teste");
        dto.setAbreviacao("TT");
        dto.setCidade("Cidade Teste");
        dto.setEstado("Estado Teste");
        dto.setLogo("logo.png");
        return dto;
    }

    private Time criarTime() {
        Time time = new Time();
        time.setId(1L);
        time.setNome("Time Teste");
        time.setAbreviacao("TT");
        time.setCidade("Cidade Teste");
        time.setEstado("Estado Teste");
        time.setLogo("logo.png");
        return time;
    }
} 