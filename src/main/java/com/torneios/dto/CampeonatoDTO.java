package com.torneios.dto;

import com.torneios.model.enums.StatusCampeonato;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CampeonatoDTO {
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotNull(message = "Data de início é obrigatória")
    private LocalDateTime dataInicio;
    
    @NotNull(message = "Data de fim é obrigatória")
    private LocalDateTime dataFim;
    
    @Min(value = 1, message = "Número de grupos deve ser maior que zero")
    private Integer numeroGrupos;
    
    @Min(value = 2, message = "Número de times por grupo deve ser maior que um")
    private Integer timesPorGrupo;
    
    @Min(value = 2, message = "Número máximo de times deve ser maior que um")
    private Integer numeroMaximoTimes;
    
    private StatusCampeonato status;
} 