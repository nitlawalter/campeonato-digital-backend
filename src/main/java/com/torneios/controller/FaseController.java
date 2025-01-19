package com.torneios.controller;

import com.torneios.dto.ErroDTO;
import com.torneios.dto.FaseDTO;
import com.torneios.model.Fase;
import com.torneios.service.FaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/campeonatos/{campeonatoId}/fases")
@Tag(name = "Fases", description = "Operações relacionadas a fases do campeonato")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class FaseController {

    private final FaseService faseService;

    @Operation(summary = "Criar fase de grupos",
            description = "Cria a fase inicial de grupos do campeonato")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Fase de grupos criada com sucesso",
                content = @Content(schema = @Schema(implementation = FaseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Campeonato já possui fases ou não está pronto para iniciar",
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping("/grupos")
    public ResponseEntity<FaseDTO> criarFaseGrupos(@PathVariable Long campeonatoId) {
        Fase fase = faseService.criarFaseGrupos(campeonatoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(fase));
    }

    @Operation(summary = "Criar próxima fase",
            description = "Cria a próxima fase do campeonato (oitavas, quartas, semifinal ou final)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Próxima fase criada com sucesso",
                content = @Content(schema = @Schema(implementation = FaseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Fase anterior não foi finalizada",
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping("/proxima")
    public ResponseEntity<FaseDTO> criarProximaFase(@PathVariable Long campeonatoId) {
        Fase fase = faseService.criarProximaFase(campeonatoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(fase));
    }

    @Operation(summary = "Gerar partidas",
            description = "Gera as partidas para uma fase específica do campeonato")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Partidas geradas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Partidas já foram geradas ou fase não está pronta",
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Fase não encontrada",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping("/{faseId}/partidas")
    public ResponseEntity<Void> gerarPartidas(@PathVariable Long campeonatoId, @PathVariable Long faseId) {
        faseService.gerarPartidas(faseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar fases",
            description = "Lista todas as fases de um campeonato")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = FaseDTO.class)))),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<FaseDTO>> listarFases(@PathVariable Long campeonatoId) {
        List<FaseDTO> fases = faseService.listarPorCampeonato(campeonatoId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(fases);
    }

    private FaseDTO toDTO(Fase fase) {
        FaseDTO dto = new FaseDTO();
        dto.setId(fase.getId());
        dto.setCampeonatoId(fase.getCampeonato().getId());
        dto.setTipo(fase.getTipo());
        dto.setNumero(fase.getNumero());
        dto.setNomeCampeonato(fase.getCampeonato().getNome());
        return dto;
    }
} 