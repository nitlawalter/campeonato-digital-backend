package com.torneios.controller;

import com.torneios.dto.ErroDTO;
import com.torneios.dto.PartidaDTO;
import com.torneios.dto.ResultadoPartidaDTO;
import com.torneios.model.Partida;
import com.torneios.service.PartidaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/campeonatos/{campeonatoId}/fases/{faseId}/partidas")
@Tag(name = "Partidas", description = "Operações relacionadas a partidas do campeonato")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaService partidaService;

    @Operation(summary = "Listar partidas da fase",
            description = "Lista todas as partidas de uma fase específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = PartidaDTO.class)))),
        @ApiResponse(responseCode = "404", description = "Fase não encontrada",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PartidaDTO>> listarPartidasDaFase(
            @PathVariable Long faseId) {
        List<PartidaDTO> partidas = partidaService.listarPorFase(faseId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(partidas);
    }

    @Operation(summary = "Buscar partida por ID",
            description = "Retorna os detalhes de uma partida específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partida encontrada com sucesso",
                content = @Content(schema = @Schema(implementation = PartidaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Partida não encontrada",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @GetMapping("/{partidaId}")
    public ResponseEntity<PartidaDTO> buscarPorId(
            @PathVariable Long partidaId) {
        Partida partida = partidaService.buscarPorId(partidaId);
        return ResponseEntity.ok(toDTO(partida));
    }

    @Operation(summary = "Registrar resultado",
            description = "Registra o resultado de uma partida")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado registrado com sucesso",
                content = @Content(schema = @Schema(implementation = PartidaDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou partida já finalizada",
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Partida não encontrada",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping("/{partidaId}/resultado")
    public ResponseEntity<PartidaDTO> registrarResultado(
            @PathVariable Long partidaId,
            @Valid @RequestBody ResultadoPartidaDTO dto) {
        Partida partida = partidaService.registrarResultado(
                partidaId,
                dto.getGolsTimeCasa(),
                dto.getGolsTimeVisitante());
        return ResponseEntity.ok(toDTO(partida));
    }

    private PartidaDTO toDTO(Partida partida) {
        PartidaDTO dto = new PartidaDTO();
        dto.setId(partida.getId());
        dto.setFaseId(partida.getFase().getId());
        dto.setTimeCasaId(partida.getTimeCasa().getId());
        dto.setTimeVisitanteId(partida.getTimeVisitante().getId());
        dto.setNomeTimeCasa(partida.getTimeCasa().getNome());
        dto.setNomeTimeVisitante(partida.getTimeVisitante().getNome());
        dto.setGolsTimeCasa(partida.getGolsTimeCasa());
        dto.setGolsTimeVisitante(partida.getGolsTimeVisitante());
        dto.setDataHora(partida.getDataHora());
        dto.setStatus(partida.getStatus());
        return dto;
    }
} 