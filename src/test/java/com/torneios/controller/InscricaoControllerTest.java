package com.torneios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.torneios.config.SecurityTestConfig;
import com.torneios.dto.InscricaoDTO;
import com.torneios.dto.TimeDTO;
import com.torneios.service.InscricaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InscricaoController.class)
@Import(SecurityTestConfig.class)
class InscricaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InscricaoService inscricaoService;

    @Test
    @WithMockUser
    void criar_DeveRetornarInscricaoCriada() throws Exception {
        // Arrange
        InscricaoDTO dto = criarInscricaoDTO();
        when(inscricaoService.inscrever(eq(1L), eq(dto.getTimeId()))).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(post("/api/campeonatos/1/inscricoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.timeId").value(dto.getTimeId()))
                .andExpect(jsonPath("$.campeonatoId").value(dto.getCampeonatoId()))
                .andExpect(jsonPath("$.aprovada").value(dto.isAprovada()));
    }

    @Test
    @WithMockUser
    void aprovar_DeveRetornarNoContent() throws Exception {
        mockMvc.perform(post("/api/campeonatos/1/inscricoes/1/aprovar"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void reprovar_DeveRetornarNoContent() throws Exception {
        mockMvc.perform(post("/api/campeonatos/1/inscricoes/1/reprovar"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void listarInscricoes_DeveRetornarListaDeInscricoes() throws Exception {
        // Arrange
        List<InscricaoDTO> inscricoes = Arrays.asList(criarInscricaoDTO(), criarInscricaoDTO());
        when(inscricaoService.listarPorCampeonato(1L)).thenReturn(inscricoes);

        // Act & Assert
        mockMvc.perform(get("/api/campeonatos/1/inscricoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].timeId").value(inscricoes.get(0).getTimeId()))
                .andExpect(jsonPath("$[1].timeId").value(inscricoes.get(1).getTimeId()));
    }

    @Test
    @WithMockUser
    void listarInscricoesAprovadas_DeveRetornarListaDeInscricoesAprovadas() throws Exception {
        // Arrange
        List<InscricaoDTO> inscricoes = Arrays.asList(criarInscricaoDTO(), criarInscricaoDTO());
        when(inscricaoService.listarInscricoesAprovadas(1L)).thenReturn(inscricoes);

        // Act & Assert
        mockMvc.perform(get("/api/campeonatos/1/inscricoes/aprovadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].timeId").value(inscricoes.get(0).getTimeId()))
                .andExpect(jsonPath("$[1].timeId").value(inscricoes.get(1).getTimeId()));
    }

    private InscricaoDTO criarInscricaoDTO() {
        InscricaoDTO dto = new InscricaoDTO();
        dto.setId(1L);
        dto.setTimeId(1L);
        dto.setCampeonatoId(1L);
        dto.setDataInscricao(LocalDateTime.now());
        dto.setAprovada(false);
        dto.setNomeCampeonato("Campeonato Teste");
        dto.setNomeTime("Time Teste");
        
        TimeDTO timeDTO = new TimeDTO();
        timeDTO.setId(1L);
        timeDTO.setNome("Time Teste");
        timeDTO.setAbreviacao("TT");
        timeDTO.setCidade("Cidade Teste");
        timeDTO.setEstado("Estado Teste");
        dto.setTime(timeDTO);
        
        return dto;
    }
} 