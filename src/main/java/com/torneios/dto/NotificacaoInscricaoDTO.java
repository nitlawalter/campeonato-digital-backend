package com.torneios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificacaoInscricaoDTO {
    private Long campeonatoId;
    private Long timeId;
    private String nomeCampeonato;
    private String nomeTime;
    private String mensagem;
} 