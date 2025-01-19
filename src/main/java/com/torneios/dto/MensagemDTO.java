package com.torneios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensagemDTO {
    private String tipo;
    private String conteudo;
    private Long campeonatoId;
    private Long timeId;
    private Long partidaId;
} 