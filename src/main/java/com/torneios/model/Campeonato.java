package com.torneios.model;

import com.torneios.model.enums.StatusCampeonato;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "campeonatos")
public class Campeonato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @Column(nullable = false)
    private Integer quantidadeMaximaTimes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCampeonato status;

    @OneToMany(mappedBy = "campeonato")
    private List<Inscricao> inscricoes;

    @OneToMany(mappedBy = "campeonato")
    private List<Fase> fases;
} 