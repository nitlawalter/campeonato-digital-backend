package com.torneios.controller;

import com.torneios.dto.CampeonatoDTO;
import com.torneios.dto.ErroDTO;
import com.torneios.model.Campeonato;
import com.torneios.service.CampeonatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/campeonatos")
@RequiredArgsConstructor
@Tag(name = "Campeonatos", description = "Operações relacionadas a campeonatos")
@SecurityRequirement(name = "bearerAuth")
public class CampeonatoController {

    private final CampeonatoService campeonatoService;

    @Operation(summary = "Criar novo campeonato",
            description = "Cria um novo campeonato com as informações fornecidas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Campeonato criado com sucesso",
                content = @Content(schema = @Schema(implementation = CampeonatoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping
    public ResponseEntity<CampeonatoDTO> criar(@Valid @RequestBody CampeonatoDTO dto) {
        Campeonato campeonato = new Campeonato();
        campeonato.setNome(dto.getNome());
        campeonato.setDataInicio(dto.getDataInicio());
        campeonato.setDataFim(dto.getDataFim());
        campeonato.setNumeroGrupos(dto.getNumeroGrupos());
        campeonato.setTimesPorGrupo(dto.getTimesPorGrupo());
        campeonato.setNumeroMaximoTimes(dto.getNumeroMaximoTimes());

        Campeonato criado = campeonatoService.criar(campeonato);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(criado));
    }

    @Operation(summary = "Atualizar campeonato", 
               description = "Atualiza as informações de um campeonato existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Campeonato atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CampeonatoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CampeonatoDTO dto) {
        Campeonato campeonato = new Campeonato();
        campeonato.setNome(dto.getNome());
        campeonato.setDataInicio(dto.getDataInicio());
        campeonato.setDataFim(dto.getDataFim());
        campeonato.setNumeroGrupos(dto.getNumeroGrupos());
        campeonato.setTimesPorGrupo(dto.getTimesPorGrupo());
        campeonato.setNumeroMaximoTimes(dto.getNumeroMaximoTimes());

        Campeonato atualizado = campeonatoService.atualizar(id, campeonato);
        return ResponseEntity.ok(toDTO(atualizado));
    }

    @Operation(summary = "Deletar campeonato", 
               description = "Remove um campeonato do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Campeonato removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado"),
        @ApiResponse(responseCode = "400", description = "Campeonato não pode ser removido no estado atual")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        campeonatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar campeonato por ID", 
               description = "Retorna os detalhes de um campeonato específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Campeonato encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CampeonatoDTO> buscarPorId(@PathVariable Long id) {
        Campeonato campeonato = campeonatoService.buscarPorId(id);
        return ResponseEntity.ok(toDTO(campeonato));
    }

    @Operation(summary = "Listar todos os campeonatos",
            description = "Retorna a lista de todos os campeonatos cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                content = @Content(schema = @Schema(implementation = CampeonatoDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<CampeonatoDTO>> listarTodos() {
        List<CampeonatoDTO> campeonatos = campeonatoService.listarTodos()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(campeonatos);
    }

    @Operation(summary = "Iniciar inscrições",
            description = "Abre o período de inscrições para um campeonato")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Inscrições iniciadas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Campeonato em estado inválido",
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping("/{id}/iniciar-inscricoes")
    public ResponseEntity<Void> iniciarInscricoes(@PathVariable Long id) {
        campeonatoService.iniciarInscricoes(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Encerrar inscrições",
            description = "Encerra o período de inscrições de um campeonato")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Inscrições encerradas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Número insuficiente de times",
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping("/{id}/encerrar-inscricoes")
    public ResponseEntity<Void> encerrarInscricoes(@PathVariable Long id) {
        campeonatoService.encerrarInscricoes(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Iniciar campeonato",
            description = "Inicia um campeonato após o encerramento das inscrições")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Campeonato iniciado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Campeonato não está pronto para iniciar",
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping("/{id}/iniciar")
    public ResponseEntity<Void> iniciarCampeonato(@PathVariable Long id) {
        campeonatoService.iniciarCampeonato(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Finalizar campeonato", 
               description = "Finaliza um campeonato em andamento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Campeonato finalizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Campeonato não pode ser finalizado no estado atual"),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado")
    })
    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarCampeonato(@PathVariable Long id) {
        campeonatoService.finalizarCampeonato(id);
        return ResponseEntity.noContent().build();
    }

    private CampeonatoDTO toDTO(Campeonato campeonato) {
        CampeonatoDTO dto = new CampeonatoDTO();
        dto.setId(campeonato.getId());
        dto.setNome(campeonato.getNome());
        dto.setDataInicio(campeonato.getDataInicio());
        dto.setDataFim(campeonato.getDataFim());
        dto.setNumeroGrupos(campeonato.getNumeroGrupos());
        dto.setTimesPorGrupo(campeonato.getTimesPorGrupo());
        dto.setNumeroMaximoTimes(campeonato.getNumeroMaximoTimes());
        dto.setStatus(campeonato.getStatus());
        return dto;
    }
} 