package com.torneios.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InscricaoDTO {
    private Long id;
    private Long timeId;
    private Long campeonatoId;
    private LocalDateTime dataInscricao;
    private boolean aprovada;
    private TimeDTO time;
    private String nomeCampeonato;
    private String nomeTime;
    private String nomeJogador;
} 