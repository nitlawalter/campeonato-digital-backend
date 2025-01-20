package com.torneios.model;

import com.torneios.model.enums.TipoFase;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "fases")
public class Fase {
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
    private Integer numeroTimes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoFase tipo;

    @ManyToOne
    @JoinColumn(name = "campeonato_id", nullable = false)
    private Campeonato campeonato;

    @OneToMany(mappedBy = "fase")
    private List<Partida> partidas;
} 