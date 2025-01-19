package com.torneios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.torneios.dto.TimeDTO;
import com.torneios.model.Time;
import com.torneios.service.TimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TimeController.class)
class TimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TimeService timeService;

    @Test
    void deveCriarTimeComSucesso() throws Exception {
        TimeDTO dto = new TimeDTO();
        dto.setNome("Time Teste");
        dto.setJogador("Jogador Teste");
        dto.setEmblema("url-emblema");

        Time time = new Time();
        time.setId(1L);
        time.setNome(dto.getNome());
        time.setJogador(dto.getJogador());
        time.setEmblema(dto.getEmblema());

        when(timeService.criar(any(Time.class))).thenReturn(time);

        mockMvc.perform(post("/api/times")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value(dto.getNome()))
                .andExpect(jsonPath("$.jogador").value(dto.getJogador()));
    }

    @Test
    void deveRetornarErroAoCriarTimeComDadosInvalidos() throws Exception {
        TimeDTO dto = new TimeDTO();
        // Nome vazio deve falhar na validação
        dto.setNome("");
        dto.setJogador("");

        mockMvc.perform(post("/api/times")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarTimesComSucesso() throws Exception {
        Time time1 = new Time();
        time1.setId(1L);
        time1.setNome("Time 1");
        time1.setJogador("Jogador 1");

        Time time2 = new Time();
        time2.setId(2L);
        time2.setNome("Time 2");
        time2.setJogador("Jogador 2");

        when(timeService.listarTodos()).thenReturn(Arrays.asList(time1, time2));

        mockMvc.perform(get("/api/times"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Time 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nome").value("Time 2"));
    }

    @Test
    void deveAtualizarTimeComSucesso() throws Exception {
        TimeDTO dto = new TimeDTO();
        dto.setNome("Time Atualizado");
        dto.setJogador("Jogador Atualizado");
        dto.setEmblema("nova-url");

        Time timeAtualizado = new Time();
        timeAtualizado.setId(1L);
        timeAtualizado.setNome(dto.getNome());
        timeAtualizado.setJogador(dto.getJogador());
        timeAtualizado.setEmblema(dto.getEmblema());

        when(timeService.atualizar(any(Long.class), any(Time.class))).thenReturn(timeAtualizado);

        mockMvc.perform(put("/api/times/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(dto.getNome()))
                .andExpect(jsonPath("$.jogador").value(dto.getJogador()));
    }
} 