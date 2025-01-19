package com.torneios.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResultadoPartidaDTO {
    @NotNull(message = "Gols do time da casa é obrigatório")
    @Min(value = 0, message = "Número de gols não pode ser negativo")
    private Integer golsTimeCasa;

    @NotNull(message = "Gols do time visitante é obrigatório")
    @Min(value = 0, message = "Número de gols não pode ser negativo")
    private Integer golsTimeVisitante;
} 