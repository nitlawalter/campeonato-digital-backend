package com.torneios.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "times")
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String jogador;

    @Column
    private String emblema;
} 