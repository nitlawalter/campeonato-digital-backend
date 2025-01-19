package com.torneios.repository;

import com.torneios.model.Inscricao;
import com.torneios.model.Campeonato;
import com.torneios.model.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    List<Inscricao> findByCampeonato(Campeonato campeonato);
    List<Inscricao> findByCampeonatoAndAprovada(Campeonato campeonato, Boolean aprovada);
    Optional<Inscricao> findByCampeonatoAndTime(Campeonato campeonato, Time time);
    long countByCampeonatoAndAprovada(Campeonato campeonato, Boolean aprovada);
} 