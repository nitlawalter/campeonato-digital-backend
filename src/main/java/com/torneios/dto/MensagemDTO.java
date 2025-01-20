package com.torneios.dto;

import com.torneios.model.enums.TipoMensagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensagemDTO {
    private TipoMensagem tipo;
    private String conteudo;
    private Long campeonatoId;
    private Long timeId;
    private Long partidaId;
} 