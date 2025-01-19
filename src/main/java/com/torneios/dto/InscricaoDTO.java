package com.torneios.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InscricaoDTO {
    private Long id;
    private Long campeonatoId;
    private Long timeId;
    private LocalDateTime dataInscricao;
    private Boolean aprovada;
    private String nomeCampeonato;
    private String nomeTime;
    private String nomeJogador;
} 