package com.torneios.dto;

import com.torneios.model.enums.TipoFase;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class FaseDTO {
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "Data de fim é obrigatória")
    private LocalDate dataFim;

    @NotNull(message = "Número de times é obrigatório")
    @Min(value = 2, message = "Número mínimo de times é 2")
    private Integer numeroTimes;

    @NotNull(message = "Tipo da fase é obrigatório")
    private TipoFase tipo;

    @NotNull(message = "ID do campeonato é obrigatório")
    private Long campeonatoId;
} 