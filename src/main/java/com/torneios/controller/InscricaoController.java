package com.torneios.controller;

import com.torneios.dto.ErroDTO;
import com.torneios.dto.InscricaoDTO;
import com.torneios.dto.TimeDTO;
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

    @PostMapping
    @Operation(summary = "Criar inscrição")
    public ResponseEntity<InscricaoDTO> criar(@PathVariable Long campeonatoId, @Valid @RequestBody InscricaoDTO dto) {
        InscricaoDTO inscricao = inscricaoService.inscrever(campeonatoId, dto.getTimeId());
        return ResponseEntity.status(HttpStatus.CREATED).body(inscricao);
    }

    @PostMapping("/{inscricaoId}/aprovar")
    @Operation(summary = "Aprovar inscrição")
    public ResponseEntity<Void> aprovar(@PathVariable Long inscricaoId) {
        inscricaoService.aprovarInscricao(inscricaoId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{inscricaoId}/reprovar")
    @Operation(summary = "Reprovar inscrição")
    public ResponseEntity<Void> reprovar(@PathVariable Long inscricaoId) {
        inscricaoService.reprovarInscricao(inscricaoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar inscrições")
    public ResponseEntity<List<InscricaoDTO>> listarInscricoes(@PathVariable Long campeonatoId) {
        return ResponseEntity.ok(inscricaoService.listarPorCampeonato(campeonatoId));
    }

    @GetMapping("/aprovadas")
    @Operation(summary = "Listar inscrições aprovadas")
    public ResponseEntity<List<InscricaoDTO>> listarInscricoesAprovadas(@PathVariable Long campeonatoId) {
        return ResponseEntity.ok(inscricaoService.listarInscricoesAprovadas(campeonatoId));
    }

    private InscricaoDTO toDTO(Inscricao inscricao) {
        InscricaoDTO dto = new InscricaoDTO();
        dto.setId(inscricao.getId());
        dto.setCampeonatoId(inscricao.getCampeonato().getId());
        dto.setTimeId(inscricao.getTime().getId());
        dto.setDataInscricao(inscricao.getDataInscricao());
        dto.setAprovada(inscricao.isAprovada());
        dto.setNomeCampeonato(inscricao.getCampeonato().getNome());
        dto.setNomeTime(inscricao.getTime().getNome());
        
        TimeDTO timeDTO = new TimeDTO();
        timeDTO.setId(inscricao.getTime().getId());
        timeDTO.setNome(inscricao.getTime().getNome());
        timeDTO.setAbreviacao(inscricao.getTime().getAbreviacao());
        timeDTO.setCidade(inscricao.getTime().getCidade());
        timeDTO.setEstado(inscricao.getTime().getEstado());
        timeDTO.setLogo(inscricao.getTime().getLogo());
        dto.setTime(timeDTO);
        
        return dto;
    }
} 