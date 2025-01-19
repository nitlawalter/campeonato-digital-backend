package com.torneios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificacaoFaseDTO {
    private Long campeonatoId;
    private String nomeCampeonato;
    private String tipoFase;
    private String mensagem;
} 