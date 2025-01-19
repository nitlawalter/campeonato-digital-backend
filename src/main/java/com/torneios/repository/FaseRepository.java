package com.torneios.repository;

import com.torneios.model.Fase;
import com.torneios.model.Campeonato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FaseRepository extends JpaRepository<Fase, Long> {
    List<Fase> findByCampeonato(Campeonato campeonato);
    List<Fase> findByCampeonatoOrderByNumeroAsc(Campeonato campeonato);
} 