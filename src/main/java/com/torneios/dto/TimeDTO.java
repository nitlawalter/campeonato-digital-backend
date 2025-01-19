package com.torneios.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TimeDTO {
    private Long id;
    
    @NotBlank(message = "Nome do time é obrigatório")
    private String nome;
    
    @NotBlank(message = "Nome do jogador é obrigatório")
    private String jogador;
    
    private String emblema;
} 