package com.torneios.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TimeDTO {
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotBlank(message = "Abreviação é obrigatória")
    private String abreviacao;
    
    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;
    
    @NotBlank(message = "Estado é obrigatório")
    private String estado;
    
    private String logo;
} 