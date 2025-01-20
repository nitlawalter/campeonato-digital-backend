package com.torneios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.torneios.config.SecurityTestConfig;
import com.torneios.dto.FaseDTO;
import com.torneios.model.enums.TipoFase;
import com.torneios.service.FaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FaseController.class)
@Import(SecurityTestConfig.class)
class FaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FaseService faseService;

    @Test
    @WithMockUser
    void criar_DeveRetornarFaseCriada() throws Exception {
        FaseDTO faseDTO = criarFaseDTO();
        when(faseService.criar(any(FaseDTO.class))).thenReturn(faseDTO);

        mockMvc.perform(post("/api/fases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(faseDTO.getNome()))
                .andExpect(jsonPath("$.numeroTimes").value(faseDTO.getNumeroTimes()))
                .andExpect(jsonPath("$.tipo").value(faseDTO.getTipo().toString()));
    }

    @Test
    @WithMockUser
    void listar_DeveRetornarTodasFases() throws Exception {
        List<FaseDTO> fases = Arrays.asList(criarFaseDTO(), criarFaseDTO());
        when(faseService.listar()).thenReturn(fases);

        mockMvc.perform(get("/api/fases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value(fases.get(0).getNome()))
                .andExpect(jsonPath("$[1].nome").value(fases.get(1).getNome()));
    }

    @Test
    @WithMockUser
    void buscarPorId_DeveRetornarFase() throws Exception {
        FaseDTO faseDTO = criarFaseDTO();
        when(faseService.buscarPorId(1L)).thenReturn(faseDTO);

        mockMvc.perform(get("/api/fases/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(faseDTO.getNome()));
    }

    @Test
    @WithMockUser
    void atualizar_DeveRetornarFaseAtualizada() throws Exception {
        FaseDTO faseDTO = criarFaseDTO();
        when(faseService.atualizar(eq(1L), any(FaseDTO.class))).thenReturn(faseDTO);

        mockMvc.perform(put("/api/fases/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(faseDTO.getNome()));
    }

    @Test
    @WithMockUser
    void excluir_DeveRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/fases/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void criarFaseGrupos_DeveRetornarFaseGruposCriada() throws Exception {
        FaseDTO faseDTO = criarFaseDTO();
        when(faseService.criarFaseGrupos(1L)).thenReturn(faseDTO);

        mockMvc.perform(post("/api/fases/grupos")
                .param("campeonatoId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(faseDTO.getNome()));
    }

    private FaseDTO criarFaseDTO() {
        FaseDTO faseDTO = new FaseDTO();
        faseDTO.setId(1L);
        faseDTO.setNome("Fase de Grupos");
        faseDTO.setDataInicio(LocalDate.now());
        faseDTO.setDataFim(LocalDate.now().plusDays(30));
        faseDTO.setNumeroTimes(16);
        faseDTO.setTipo(TipoFase.GRUPOS);
        faseDTO.setCampeonatoId(1L);
        return faseDTO;
    }
} 