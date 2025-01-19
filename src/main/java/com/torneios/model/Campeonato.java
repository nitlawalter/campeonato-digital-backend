package com.torneios.model;

import com.torneios.model.enums.StatusCampeonato;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
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
    private LocalDateTime dataInicio;

    @Column(nullable = false)
    private LocalDateTime dataFim;

    @Column(nullable = false)
    private Integer numeroGrupos;

    @Column(nullable = false)
    private Integer timesPorGrupo;

    @Column(nullable = false)
    private Integer numeroMaximoTimes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCampeonato status;

    @OneToMany(mappedBy = "campeonato")
    private List<Inscricao> inscricoes;

    @OneToMany(mappedBy = "campeonato")
    private List<Fase> fases;
} 