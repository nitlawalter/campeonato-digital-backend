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
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fases")
@Tag(name = "Fases", description = "Operações relacionadas a fases do campeonato")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class FaseController {

    private final FaseService faseService;

    @PostMapping
    @Operation(summary = "Criar uma nova fase")
    public ResponseEntity<FaseDTO> criar(@RequestBody @Valid FaseDTO faseDTO) {
        FaseDTO faseCriada = faseService.criar(faseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(faseCriada);
    }

    @GetMapping
    @Operation(summary = "Listar todas as fases")
    public ResponseEntity<List<FaseDTO>> listar() {
        return ResponseEntity.ok(faseService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar fase por ID")
    public ResponseEntity<FaseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(faseService.buscarPorId(id));
    }

    @GetMapping("/campeonato/{campeonatoId}")
    @Operation(summary = "Listar fases por campeonato")
    public ResponseEntity<List<FaseDTO>> listarPorCampeonato(@PathVariable Long campeonatoId) {
        return ResponseEntity.ok(faseService.listarPorCampeonato(campeonatoId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma fase")
    public ResponseEntity<FaseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid FaseDTO faseDTO) {
        return ResponseEntity.ok(faseService.atualizar(id, faseDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma fase")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        faseService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar uma fase")
    public ResponseEntity<Void> iniciarFase(@PathVariable Long id) {
        faseService.iniciarFase(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar uma fase")
    public ResponseEntity<Void> finalizarFase(@PathVariable Long id) {
        faseService.finalizarFase(id);
        return ResponseEntity.noContent().build();
    }

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
        FaseDTO faseDto = faseService.criarFaseGrupos(campeonatoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(faseDto);
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
        FaseDTO fase = faseService.criarProximaFase(campeonatoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(fase);
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

    private FaseDTO toDTO(Fase fase) {
        FaseDTO dto = new FaseDTO();
        dto.setId(fase.getId());
        dto.setNome(fase.getNome());
        dto.setDataInicio(fase.getDataInicio());
        dto.setDataFim(fase.getDataFim());
        dto.setNumeroTimes(fase.getNumeroTimes());
        dto.setTipo(fase.getTipo());
        dto.setCampeonatoId(fase.getCampeonato().getId());
        return dto;
    }
} 