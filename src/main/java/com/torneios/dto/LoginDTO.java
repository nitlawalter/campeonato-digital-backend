package com.torneios.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @JsonProperty
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @JsonProperty
    private String senha;
} 