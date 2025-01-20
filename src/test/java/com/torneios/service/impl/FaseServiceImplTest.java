package com.torneios.service.impl;

import com.torneios.dto.FaseDTO;
import com.torneios.exception.NegocioException;
import com.torneios.model.Campeonato;
import com.torneios.model.Fase;
import com.torneios.model.enums.StatusCampeonato;
import com.torneios.model.enums.TipoFase;
import com.torneios.repository.CampeonatoRepository;
import com.torneios.repository.FaseRepository;
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
class FaseServiceImplTest {

    @Mock
    private FaseRepository faseRepository;

    @Mock
    private CampeonatoRepository campeonatoRepository;

    @InjectMocks
    private FaseServiceImpl faseService;

    @Test
    void criar_DeveRetornarFaseCriada() {
        // Arrange
        FaseDTO dto = criarFaseDTO();
        Campeonato campeonato = criarCampeonato();
        
        when(campeonatoRepository.findById(dto.getCampeonatoId())).thenReturn(Optional.of(campeonato));
        when(faseRepository.save(any(Fase.class))).thenAnswer(i -> {
            Fase fase = i.getArgument(0);
            fase.setId(1L);
            return fase;
        });

        // Act
        FaseDTO result = faseService.criar(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getNome(), result.getNome());
        assertEquals(dto.getNumeroTimes(), result.getNumeroTimes());
        assertEquals(dto.getTipo(), result.getTipo());
    }

    @Test
    void criar_DeveRetornarErroQuandoCampeonatoNaoExiste() {
        // Arrange
        FaseDTO dto = criarFaseDTO();
        when(campeonatoRepository.findById(dto.getCampeonatoId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NegocioException.class, () -> faseService.criar(dto));
    }

    @Test
    void criarFaseGrupos_DeveRetornarFaseGruposCriada() {
        // Arrange
        Campeonato campeonato = criarCampeonato();
        when(campeonatoRepository.findById(1L)).thenReturn(Optional.of(campeonato));
        when(faseRepository.save(any(Fase.class))).thenAnswer(i -> {
            Fase fase = i.getArgument(0);
            fase.setId(1L);
            return fase;
        });

        // Act
        FaseDTO result = faseService.criarFaseGrupos(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Fase de Grupos", result.getNome());
        assertEquals(TipoFase.GRUPOS, result.getTipo());
        assertEquals(campeonato.getQuantidadeMaximaTimes(), result.getNumeroTimes());
    }

    @Test
    void listarPorCampeonato_DeveRetornarListaDeFases() {
        // Arrange
        Campeonato campeonato = criarCampeonato();
        Fase fase = criarFase(campeonato);
        
        when(campeonatoRepository.findById(1L)).thenReturn(Optional.of(campeonato));
        when(faseRepository.findByCampeonato(campeonato)).thenReturn(Arrays.asList(fase));

        // Act
        List<FaseDTO> result = faseService.listarPorCampeonato(1L);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(fase.getNome(), result.get(0).getNome());
    }

    private FaseDTO criarFaseDTO() {
        FaseDTO dto = new FaseDTO();
        dto.setNome("Fase de Grupos");
        dto.setDataInicio(LocalDate.now().plusDays(1));
        dto.setDataFim(LocalDate.now().plusMonths(1));
        dto.setNumeroTimes(16);
        dto.setTipo(TipoFase.GRUPOS);
        dto.setCampeonatoId(1L);
        return dto;
    }

    private Campeonato criarCampeonato() {
        Campeonato campeonato = new Campeonato();
        campeonato.setId(1L);
        campeonato.setNome("Campeonato Teste");
        campeonato.setDataInicio(LocalDate.now().plusDays(1));
        campeonato.setDataFim(LocalDate.now().plusMonths(1));
        campeonato.setQuantidadeMaximaTimes(16);
        campeonato.setStatus(StatusCampeonato.INSCRICOES_ENCERRADAS);
        return campeonato;
    }

    private Fase criarFase(Campeonato campeonato) {
        Fase fase = new Fase();
        fase.setId(1L);
        fase.setNome("Fase de Grupos");
        fase.setDataInicio(LocalDate.now().plusDays(1));
        fase.setDataFim(LocalDate.now().plusMonths(1));
        fase.setNumeroTimes(16);
        fase.setTipo(TipoFase.GRUPOS);
        fase.setCampeonato(campeonato);
        return fase;
    }
} 