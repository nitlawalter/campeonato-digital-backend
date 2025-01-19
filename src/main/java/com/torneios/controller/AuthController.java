package com.torneios.controller;

import com.torneios.dto.ErroDTO;
import com.torneios.dto.LoginDTO;
import com.torneios.dto.RegistroDTO;
import com.torneios.dto.TokenDTO;
import com.torneios.service.AuthService;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação e registro de usuários")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Registrar novo usuário",
            description = "Cria um novo usuário no sistema e retorna um token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso",
                content = @Content(schema = @Schema(implementation = TokenDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<TokenDTO> registrar(@Valid @RequestBody RegistroDTO request) {
        String token = authService.registrar(request);
        return ResponseEntity.ok(new TokenDTO(token));
    }

    @Operation(summary = "Realizar login",
            description = "Autentica um usuário e retorna um token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                content = @Content(schema = @Schema(implementation = TokenDTO.class))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO request) {
        String token = authService.autenticar(request);
        return ResponseEntity.ok(new TokenDTO(token));
    }
} 