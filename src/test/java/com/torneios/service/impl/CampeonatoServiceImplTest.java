package com.torneios.service.impl;

import com.torneios.dto.CampeonatoDTO;
import com.torneios.exception.NegocioException;
import com.torneios.model.Campeonato;
import com.torneios.model.enums.StatusCampeonato;
import com.torneios.repository.CampeonatoRepository;
import com.torneios.repository.InscricaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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

    @Test
    void criar_DeveRetornarCampeonatoCriado() {
        // Arrange
        CampeonatoDTO dto = criarCampeonatoDTO();
        when(campeonatoRepository.existsByNome(dto.getNome())).thenReturn(false);
        when(campeonatoRepository.save(any(Campeonato.class))).thenAnswer(i -> {
            Campeonato campeonato = i.getArgument(0);
            campeonato.setId(1L);
            return campeonato;
        });

        // Act
        CampeonatoDTO result = campeonatoService.criar(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getNome(), result.getNome());
        assertEquals(dto.getQuantidadeMaximaTimes(), result.getQuantidadeMaximaTimes());
        assertEquals(StatusCampeonato.CRIADO, result.getStatus());
    }

    @Test
    void criar_DeveRetornarErroQuandoCampeonatoJaExiste() {
        // Arrange
        CampeonatoDTO dto = criarCampeonatoDTO();
        when(campeonatoRepository.existsByNome(dto.getNome())).thenReturn(true);

        // Act & Assert
        assertThrows(NegocioException.class, () -> campeonatoService.criar(dto));
        verify(campeonatoRepository, never()).save(any(Campeonato.class));
    }

    @Test
    void iniciarInscricoes_DeveAtualizarStatus() {
        // Arrange
        Campeonato campeonato = criarCampeonato();
        campeonato.setStatus(StatusCampeonato.CRIADO);
        
        when(campeonatoRepository.findById(1L)).thenReturn(Optional.of(campeonato));
        when(campeonatoRepository.save(any(Campeonato.class))).thenReturn(campeonato);

        // Act
        campeonatoService.iniciarInscricoes(1L);

        // Assert
        assertEquals(StatusCampeonato.INSCRICOES_ABERTAS, campeonato.getStatus());
        verify(campeonatoRepository).save(campeonato);
    }

    @Test
    void encerrarInscricoes_DeveAtualizarStatus() {
        // Arrange
        Campeonato campeonato = criarCampeonato();
        campeonato.setStatus(StatusCampeonato.INSCRICOES_ABERTAS);
        
        when(campeonatoRepository.findById(1L)).thenReturn(Optional.of(campeonato));
        when(inscricaoRepository.countByCampeonatoAndAprovada(eq(campeonato), eq(true))).thenReturn(2L);
        when(campeonatoRepository.save(any(Campeonato.class))).thenReturn(campeonato);

        // Act
        campeonatoService.encerrarInscricoes(1L);

        // Assert
        assertEquals(StatusCampeonato.INSCRICOES_ENCERRADAS, campeonato.getStatus());
        verify(campeonatoRepository).save(campeonato);
    }

    @Test
    void iniciarCampeonato_DeveAtualizarStatus() {
        // Arrange
        Campeonato campeonato = criarCampeonato();
        campeonato.setStatus(StatusCampeonato.INSCRICOES_ENCERRADAS);
        
        when(campeonatoRepository.findById(1L)).thenReturn(Optional.of(campeonato));
        when(campeonatoRepository.save(any(Campeonato.class))).thenReturn(campeonato);

        // Act
        campeonatoService.iniciarCampeonato(1L);

        // Assert
        assertEquals(StatusCampeonato.EM_ANDAMENTO, campeonato.getStatus());
        verify(campeonatoRepository).save(campeonato);
    }

    private CampeonatoDTO criarCampeonatoDTO() {
        CampeonatoDTO dto = new CampeonatoDTO();
        dto.setNome("Campeonato Teste");
        dto.setDataInicio(LocalDate.now().plusDays(1));
        dto.setDataFim(LocalDate.now().plusMonths(1));
        dto.setQuantidadeMaximaTimes(16);
        return dto;
    }

    private Campeonato criarCampeonato() {
        Campeonato campeonato = new Campeonato();
        campeonato.setId(1L);
        campeonato.setNome("Campeonato Teste");
        campeonato.setDataInicio(LocalDate.now().plusDays(1));
        campeonato.setDataFim(LocalDate.now().plusMonths(1));
        campeonato.setQuantidadeMaximaTimes(16);
        campeonato.setStatus(StatusCampeonato.CRIADO);
        return campeonato;
    }
} 