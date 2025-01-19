package com.torneios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.torneios.dto.CampeonatoDTO;
import com.torneios.model.Campeonato;
import com.torneios.service.CampeonatoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CampeonatoController.class)
class CampeonatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CampeonatoService campeonatoService;

    @Test
    void deveCriarCampeonatoComSucesso() throws Exception {
        CampeonatoDTO dto = new CampeonatoDTO();
        dto.setNome("Campeonato Teste");
        dto.setDataInicio(LocalDateTime.now().plusDays(1));
        dto.setDataFim(LocalDateTime.now().plusDays(30));
        dto.setNumeroGrupos(4);
        dto.setTimesPorGrupo(4);
        dto.setNumeroMaximoTimes(16);

        Campeonato campeonato = new Campeonato();
        campeonato.setId(1L);
        campeonato.setNome(dto.getNome());
        // ... setar outros campos

        when(campeonatoService.criar(any(Campeonato.class))).thenReturn(campeonato);

        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value(dto.getNome()));
    }

    @Test
    void deveRetornarErroAoCriarCampeonatoComDadosInvalidos() throws Exception {
        CampeonatoDTO dto = new CampeonatoDTO();
        // Nome vazio deve falhar na validação
        dto.setNome("");

        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
} 