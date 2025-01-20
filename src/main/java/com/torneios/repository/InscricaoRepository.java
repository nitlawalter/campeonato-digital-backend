package com.torneios.repository;

import com.torneios.model.Campeonato;
import com.torneios.model.Inscricao;
import com.torneios.model.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    boolean existsByTimeAndCampeonato(Time time, Campeonato campeonato);
    long countByCampeonatoAndAprovada(Campeonato campeonato, boolean aprovada);
    List<Inscricao> findByCampeonato(Campeonato campeonato);
    List<Inscricao> findByCampeonatoAndAprovada(Campeonato campeonato, boolean aprovada);
} 