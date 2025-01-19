package com.torneios.controller;

import com.torneios.dto.ErroDTO;
import com.torneios.dto.TimeDTO;
import com.torneios.model.Time;
import com.torneios.service.TimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/times")
@RequiredArgsConstructor
@Tag(name = "Times", description = "Operações relacionadas a times")
@SecurityRequirement(name = "bearerAuth")
public class TimeController {

    private final TimeService timeService;

    @Operation(summary = "Criar novo time",
            description = "Cria um novo time com as informações fornecidas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Time criado com sucesso",
                content = @Content(schema = @Schema(implementation = TimeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping
    public ResponseEntity<TimeDTO> criar(@Valid @RequestBody TimeDTO dto) {
        Time time = new Time();
        time.setNome(dto.getNome());
        time.setJogador(dto.getJogador());
        time.setEmblema(dto.getEmblema());

        Time criado = timeService.criar(time);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(criado));
    }

    @Operation(summary = "Atualizar time",
            description = "Atualiza as informações de um time existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Time atualizado com sucesso",
                content = @Content(schema = @Schema(implementation = TimeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Time não encontrado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<TimeDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TimeDTO dto) {
        Time time = new Time();
        time.setNome(dto.getNome());
        time.setJogador(dto.getJogador());
        time.setEmblema(dto.getEmblema());

        Time atualizado = timeService.atualizar(id, time);
        return ResponseEntity.ok(toDTO(atualizado));
    }

    @Operation(summary = "Deletar time",
            description = "Remove um time do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Time removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Time não encontrado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "400", description = "Time não pode ser removido",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        timeService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar time por ID",
            description = "Retorna os detalhes de um time específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Time encontrado com sucesso",
                content = @Content(schema = @Schema(implementation = TimeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Time não encontrado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TimeDTO> buscarPorId(@PathVariable Long id) {
        Time time = timeService.buscarPorId(id);
        return ResponseEntity.ok(toDTO(time));
    }

    @Operation(summary = "Listar todos os times",
            description = "Retorna a lista de todos os times cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = TimeDTO.class))))
    })
    @GetMapping
    public ResponseEntity<List<TimeDTO>> listarTodos() {
        List<TimeDTO> times = timeService.listarTodos()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(times);
    }

    private TimeDTO toDTO(Time time) {
        TimeDTO dto = new TimeDTO();
        dto.setId(time.getId());
        dto.setNome(time.getNome());
        dto.setJogador(time.getJogador());
        dto.setEmblema(time.getEmblema());
        return dto;
    }
} 