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

    @PostMapping
    @Operation(summary = "Criar um novo time")
    public ResponseEntity<TimeDTO> criar(@RequestBody @Valid TimeDTO timeDTO) {
        return ResponseEntity.ok(timeService.criar(timeDTO));
    }

    @GetMapping
    @Operation(summary = "Listar todos os times")
    public ResponseEntity<List<TimeDTO>> listar() {
        return ResponseEntity.ok(timeService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar time por ID")
    public ResponseEntity<TimeDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(timeService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um time")
    public ResponseEntity<TimeDTO> atualizar(@PathVariable Long id, @RequestBody @Valid TimeDTO timeDTO) {
        return ResponseEntity.ok(timeService.atualizar(id, timeDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um time")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        timeService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private TimeDTO toDTO(Time time) {
        TimeDTO dto = new TimeDTO();
        dto.setId(time.getId());
        dto.setNome(time.getNome());
        dto.setAbreviacao(time.getAbreviacao());
        dto.setCidade(time.getCidade());
        dto.setEstado(time.getEstado());
        dto.setLogo(time.getLogo());

        return dto;
    }
} 