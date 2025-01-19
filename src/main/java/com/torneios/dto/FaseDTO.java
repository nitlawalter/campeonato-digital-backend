package com.torneios.dto;

import com.torneios.model.enums.TipoFase;
import lombok.Data;

@Data
public class FaseDTO {
    private Long id;
    private Long campeonatoId;
    private TipoFase tipo;
    private Integer numero;
    private String nomeCampeonato;
} 