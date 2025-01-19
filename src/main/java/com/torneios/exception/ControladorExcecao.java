package com.torneios.exception;

import com.torneios.dto.ErroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControladorExcecao {

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErroDTO> handleNegocioException(NegocioException ex) {
        ErroDTO erro = new ErroDTO(ex.getMessage(), null, null);
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroDTO>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErroDTO> erros = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(this::mapearErro)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(erros);
    }

    private ErroDTO mapearErro(FieldError error) {
        return new ErroDTO(
            error.getDefaultMessage(),
            error.getField(),
            error.getRejectedValue() != null ? error.getRejectedValue().toString() : null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroDTO> handleException(Exception ex) {
        ErroDTO erro = new ErroDTO("Erro interno do servidor", null, null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
} 