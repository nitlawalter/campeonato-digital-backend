package com.torneios.dto;

import com.torneios.model.enums.StatusPartida;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PartidaDTO {
    private Long id;
    private Long faseId;
    private Long timeCasaId;
    private Long timeVisitanteId;
    private String nomeTimeCasa;
    private String nomeTimeVisitante;
    private Integer golsTimeCasa;
    private Integer golsTimeVisitante;
    private LocalDateTime dataHora;
    private StatusPartida status;
} 