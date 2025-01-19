package com.torneios.controller;

import com.torneios.dto.ErroDTO;
import com.torneios.dto.InscricaoDTO;
import com.torneios.model.Inscricao;
import com.torneios.service.InscricaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/campeonatos/{campeonatoId}/inscricoes")
@Tag(name = "Inscrições", description = "Operações relacionadas a inscrições em campeonatos")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class InscricaoController {

    private final InscricaoService inscricaoService;

    @Operation(summary = "Criar inscrição",
            description = "Inscreve um time em um campeonato")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Inscrição realizada com sucesso",
                content = @Content(schema = @Schema(implementation = InscricaoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou campeonato não está aceitando inscrições",
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Campeonato ou time não encontrado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping
    public ResponseEntity<InscricaoDTO> criar(@PathVariable Long campeonatoId, @Valid @RequestBody InscricaoDTO dto) {
        Inscricao inscricao = inscricaoService.inscrever(campeonatoId, dto.getTimeId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(inscricao));
    }

    @Operation(summary = "Aprovar inscrição",
            description = "Aprova a inscrição de um time no campeonato")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Inscrição aprovada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Campeonato não está mais aceitando inscrições",
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Inscrição não encontrada",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping("/{inscricaoId}/aprovar")
    public ResponseEntity<Void> aprovar(@PathVariable Long campeonatoId, @PathVariable Long inscricaoId) {
        inscricaoService.aprovarInscricao(inscricaoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reprovar inscrição",
            description = "Reprova a inscrição de um time no campeonato")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Inscrição reprovada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Inscrição não encontrada",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping("/{inscricaoId}/reprovar")
    public ResponseEntity<Void> reprovar(@PathVariable Long campeonatoId, @PathVariable Long inscricaoId) {
        inscricaoService.reprovarInscricao(inscricaoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar inscrições",
            description = "Lista todas as inscrições de um campeonato")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = InscricaoDTO.class)))),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<InscricaoDTO>> listarInscricoes(@PathVariable Long campeonatoId) {
        List<InscricaoDTO> inscricoes = inscricaoService.listarPorCampeonato(campeonatoId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(inscricoes);
    }

    @Operation(summary = "Listar inscrições aprovadas",
            description = "Lista todas as inscrições aprovadas de um campeonato")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = InscricaoDTO.class)))),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @GetMapping("/aprovadas")
    public ResponseEntity<List<InscricaoDTO>> listarInscricoesAprovadas(@PathVariable Long campeonatoId) {
        List<InscricaoDTO> inscricoes = inscricaoService.listarInscricoesAprovadas(campeonatoId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(inscricoes);
    }

    private InscricaoDTO toDTO(Inscricao inscricao) {
        InscricaoDTO dto = new InscricaoDTO();
        dto.setId(inscricao.getId());
        dto.setCampeonatoId(inscricao.getCampeonato().getId());
        dto.setTimeId(inscricao.getTime().getId());
        dto.setDataInscricao(inscricao.getDataInscricao());
        dto.setAprovada(inscricao.getAprovada());
        dto.setNomeCampeonato(inscricao.getCampeonato().getNome());
        dto.setNomeTime(inscricao.getTime().getNome());
        dto.setNomeJogador(inscricao.getTime().getJogador());
        return dto;
    }
} 