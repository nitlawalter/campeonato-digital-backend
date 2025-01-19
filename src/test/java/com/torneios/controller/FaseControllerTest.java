package com.torneios.controller;

import com.torneios.model.Fase;
import com.torneios.model.Campeonato;
import com.torneios.model.enums.TipoFase;
import com.torneios.service.FaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FaseController.class)
class FaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FaseService faseService;

    private Fase fase;
    private Campeonato campeonato;

    @BeforeEach
    void setUp() {
        campeonato = new Campeonato();
        campeonato.setId(1L);
        campeonato.setNome("Campeonato Teste");

        fase = new Fase();
        fase.setId(1L);
        fase.setCampeonato(campeonato);
        fase.setTipo(TipoFase.GRUPOS);
        fase.setNumero(1);
    }

    @Test
    void deveCriarFaseGruposComSucesso() throws Exception {
        when(faseService.criarFaseGrupos(anyLong())).thenReturn(fase);

        mockMvc.perform(post("/api/campeonatos/1/fases/grupos"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipo").value("GRUPOS"))
                .andExpect(jsonPath("$.numero").value(1));
    }

    @Test
    void deveCriarProximaFaseComSucesso() throws Exception {
        fase.setTipo(TipoFase.OITAVAS);
        fase.setNumero(2);
        when(faseService.criarProximaFase(anyLong())).thenReturn(fase);

        mockMvc.perform(post("/api/campeonatos/1/fases/proxima"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("OITAVAS"))
                .andExpect(jsonPath("$.numero").value(2));
    }

    @Test
    void deveListarFasesPorCampeonatoComSucesso() throws Exception {
        when(faseService.listarPorCampeonato(anyLong())).thenReturn(Arrays.asList(fase));

        mockMvc.perform(get("/api/campeonatos/1/fases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].tipo").value("GRUPOS"))
                .andExpect(jsonPath("$[0].campeonatoId").value(1L));
    }

    @Test
    void deveGerarPartidasComSucesso() throws Exception {
        mockMvc.perform(post("/api/campeonatos/1/fases/1/gerar-partidas"))
                .andExpect(status().isNoContent());
    }
} 