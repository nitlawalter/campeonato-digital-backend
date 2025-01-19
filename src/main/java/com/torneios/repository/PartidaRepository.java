package com.torneios.repository;

import com.torneios.model.Partida;
import com.torneios.model.Fase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByFase(Fase fase);
    List<Partida> findByFaseId(Long faseId);
} 