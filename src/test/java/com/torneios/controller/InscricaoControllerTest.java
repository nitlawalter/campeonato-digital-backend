package com.torneios.controller;

import com.torneios.model.Campeonato;
import com.torneios.model.Inscricao;
import com.torneios.model.Time;
import com.torneios.service.InscricaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InscricaoController.class)
class InscricaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InscricaoService inscricaoService;

    private Inscricao inscricao;
    private Campeonato campeonato;
    private Time time;

    @BeforeEach
    void setUp() {
        campeonato = new Campeonato();
        campeonato.setId(1L);
        campeonato.setNome("Campeonato Teste");

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
    void deveInscreverTimeComSucesso() throws Exception {
        when(inscricaoService.inscrever(anyLong(), anyLong())).thenReturn(inscricao);

        mockMvc.perform(post("/api/campeonatos/1/inscricoes")
                .param("timeId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.timeId").value(1L))
                .andExpect(jsonPath("$.campeonatoId").value(1L))
                .andExpect(jsonPath("$.aprovada").value(false));
    }

    @Test
    void deveAprovarInscricaoComSucesso() throws Exception {
        mockMvc.perform(post("/api/campeonatos/1/inscricoes/1/aprovar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveListarInscricoesComSucesso() throws Exception {
        when(inscricaoService.listarPorCampeonato(anyLong())).thenReturn(Arrays.asList(inscricao));

        mockMvc.perform(get("/api/campeonatos/1/inscricoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].timeId").value(1L))
                .andExpect(jsonPath("$[0].campeonatoId").value(1L))
                .andExpect(jsonPath("$[0].nomeTime").value("Time Teste"))
                .andExpect(jsonPath("$[0].nomeCampeonato").value("Campeonato Teste"));
    }

    @Test
    void deveListarInscricoesAprovadasComSucesso() throws Exception {
        inscricao.setAprovada(true);
        when(inscricaoService.listarInscricoesAprovadas(anyLong())).thenReturn(Arrays.asList(inscricao));

        mockMvc.perform(get("/api/campeonatos/1/inscricoes/aprovadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].aprovada").value(true));
    }
} 