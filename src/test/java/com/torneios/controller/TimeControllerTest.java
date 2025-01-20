package com.torneios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.torneios.config.SecurityTestConfig;
import com.torneios.dto.TimeDTO;
import com.torneios.service.TimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TimeController.class)
@Import(SecurityTestConfig.class)
class TimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeService timeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void criarTime_DeveRetornarTimeDTO() throws Exception {
        TimeDTO timeDTO = criarTimeDTO();
        when(timeService.criar(any(TimeDTO.class))).thenReturn(timeDTO);

        mockMvc.perform(post("/api/times")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(timeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(timeDTO.getNome()))
                .andExpect(jsonPath("$.abreviacao").value(timeDTO.getAbreviacao()))
                .andExpect(jsonPath("$.cidade").value(timeDTO.getCidade()))
                .andExpect(jsonPath("$.estado").value(timeDTO.getEstado()));
    }

    @Test
    @WithMockUser
    void listarTimes_DeveRetornarListaDeTimesDTO() throws Exception {
        TimeDTO timeDTO = criarTimeDTO();
        when(timeService.listar()).thenReturn(Arrays.asList(timeDTO));

        mockMvc.perform(get("/api/times"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value(timeDTO.getNome()))
                .andExpect(jsonPath("$[0].abreviacao").value(timeDTO.getAbreviacao()))
                .andExpect(jsonPath("$[0].cidade").value(timeDTO.getCidade()))
                .andExpect(jsonPath("$[0].estado").value(timeDTO.getEstado()));
    }

    @Test
    @WithMockUser
    void buscarTimePorId_DeveRetornarTimeDTO() throws Exception {
        TimeDTO timeDTO = criarTimeDTO();
        when(timeService.buscarPorId(1L)).thenReturn(timeDTO);

        mockMvc.perform(get("/api/times/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(timeDTO.getNome()))
                .andExpect(jsonPath("$.abreviacao").value(timeDTO.getAbreviacao()))
                .andExpect(jsonPath("$.cidade").value(timeDTO.getCidade()))
                .andExpect(jsonPath("$.estado").value(timeDTO.getEstado()));
    }

    @Test
    @WithMockUser
    void atualizarTime_DeveRetornarTimeAtualizado() throws Exception {
        TimeDTO timeDTO = criarTimeDTO();
        when(timeService.atualizar(eq(1L), any(TimeDTO.class))).thenReturn(timeDTO);

        mockMvc.perform(put("/api/times/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(timeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(timeDTO.getNome()))
                .andExpect(jsonPath("$.abreviacao").value(timeDTO.getAbreviacao()))
                .andExpect(jsonPath("$.cidade").value(timeDTO.getCidade()))
                .andExpect(jsonPath("$.estado").value(timeDTO.getEstado()));
    }

    @Test
    @WithMockUser
    void excluirTime_DeveRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/times/1"))
                .andExpect(status().isNoContent());
    }

    private TimeDTO criarTimeDTO() {
        TimeDTO timeDTO = new TimeDTO();
        timeDTO.setId(1L);
        timeDTO.setNome("Flamengo");
        timeDTO.setAbreviacao("FLA");
        timeDTO.setCidade("Rio de Janeiro");
        timeDTO.setEstado("RJ");
        timeDTO.setLogo("https://url-do-logo.com/flamengo.png");
        return timeDTO;
    }
} 