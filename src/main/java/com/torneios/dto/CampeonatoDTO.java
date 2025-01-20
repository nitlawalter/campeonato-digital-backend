package com.torneios.dto;

import com.torneios.model.enums.StatusCampeonato;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CampeonatoDTO {
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;
    
    @NotNull(message = "Data de fim é obrigatória")
    private LocalDate dataFim;
    
    @NotNull(message = "Quantidade máxima de times é obrigatória")
    @Min(value = 2, message = "Quantidade mínima de times é 2")
    private Integer quantidadeMaximaTimes;
    
    private StatusCampeonato status;
} 