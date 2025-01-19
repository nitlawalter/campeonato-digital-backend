package com.torneios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.torneios.dto.ResultadoPartidaDTO;
import com.torneios.model.Partida;
import com.torneios.model.Fase;
import com.torneios.model.Time;
import com.torneios.model.enums.StatusPartida;
import com.torneios.service.PartidaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartidaController.class)
class PartidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PartidaService partidaService;

    private Partida partida;
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

        Fase fase = new Fase();
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
    void deveRegistrarResultadoComSucesso() throws Exception {
        ResultadoPartidaDTO dto = new ResultadoPartidaDTO();
        dto.setGolsTimeCasa(2);
        dto.setGolsTimeVisitante(1);

        partida.setGolsTimeCasa(dto.getGolsTimeCasa());
        partida.setGolsTimeVisitante(dto.getGolsTimeVisitante());
        partida.setStatus(StatusPartida.FINALIZADA);

        when(partidaService.registrarResultado(anyLong(), any(Integer.class), any(Integer.class)))
                .thenReturn(partida);

        mockMvc.perform(put("/api/fases/1/partidas/1/resultado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.golsTimeCasa").value(2))
                .andExpect(jsonPath("$.golsTimeVisitante").value(1))
                .andExpect(jsonPath("$.status").value("FINALIZADA"));
    }

    @Test
    void deveListarPartidasPorFaseComSucesso() throws Exception {
        when(partidaService.listarPorFase(anyLong())).thenReturn(Arrays.asList(partida));

        mockMvc.perform(get("/api/fases/1/partidas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].timeCasaId").value(timeCasa.getId()))
                .andExpect(jsonPath("$[0].timeVisitanteId").value(timeVisitante.getId()))
                .andExpect(jsonPath("$[0].nomeTimeCasa").value(timeCasa.getNome()))
                .andExpect(jsonPath("$[0].nomeTimeVisitante").value(timeVisitante.getNome()));
    }

    @Test
    void deveBuscarPartidaPorIdComSucesso() throws Exception {
        when(partidaService.buscarPorId(anyLong())).thenReturn(partida);

        mockMvc.perform(get("/api/fases/1/partidas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("AGENDADA"));
    }
} 