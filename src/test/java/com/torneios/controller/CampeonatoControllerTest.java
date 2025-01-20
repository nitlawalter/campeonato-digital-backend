package com.torneios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.torneios.config.SecurityTestConfig;
import com.torneios.dto.CampeonatoDTO;
import com.torneios.model.enums.StatusCampeonato;
import com.torneios.service.CampeonatoService;
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

@WebMvcTest(CampeonatoController.class)
@Import(SecurityTestConfig.class)
class CampeonatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CampeonatoService campeonatoService;

    @Test
    @WithMockUser
    void criar_DeveRetornarCampeonatoCriado() throws Exception {
        CampeonatoDTO dto = criarCampeonatoDTO();
        when(campeonatoService.criar(any(CampeonatoDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(dto.getNome()))
                .andExpect(jsonPath("$.quantidadeMaximaTimes").value(dto.getQuantidadeMaximaTimes()))
                .andExpect(jsonPath("$.status").value(dto.getStatus().toString()));
    }

    @Test
    @WithMockUser
    void listar_DeveRetornarTodosCampeonatos() throws Exception {
        List<CampeonatoDTO> campeonatos = Arrays.asList(criarCampeonatoDTO(), criarCampeonatoDTO());
        when(campeonatoService.listarTodos()).thenReturn(Arrays.asList()); // Ajustar conforme necessário

        mockMvc.perform(get("/api/campeonatos"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void buscarPorId_DeveRetornarCampeonato() throws Exception {
        CampeonatoDTO dto = criarCampeonatoDTO();
        when(campeonatoService.buscarPorId(1L)).thenReturn(null); // Ajustar conforme necessário

        mockMvc.perform(get("/api/campeonatos/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void iniciarInscricoes_DeveRetornarNoContent() throws Exception {
        mockMvc.perform(post("/api/campeonatos/1/iniciar-inscricoes"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void encerrarInscricoes_DeveRetornarNoContent() throws Exception {
        mockMvc.perform(post("/api/campeonatos/1/encerrar-inscricoes"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void iniciarCampeonato_DeveRetornarNoContent() throws Exception {
        mockMvc.perform(post("/api/campeonatos/1/iniciar"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void finalizarCampeonato_DeveRetornarNoContent() throws Exception {
        mockMvc.perform(post("/api/campeonatos/1/finalizar"))
                .andExpect(status().isNoContent());
    }

    private CampeonatoDTO criarCampeonatoDTO() {
        CampeonatoDTO dto = new CampeonatoDTO();
        dto.setId(1L);
        dto.setNome("Campeonato Teste");
        dto.setDataInicio(LocalDate.now().plusDays(1));
        dto.setDataFim(LocalDate.now().plusMonths(1));
        dto.setQuantidadeMaximaTimes(16);
        dto.setStatus(StatusCampeonato.CRIADO);
        return dto;
    }
} 