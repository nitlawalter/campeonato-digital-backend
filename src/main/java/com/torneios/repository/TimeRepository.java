package com.torneios.repository;

import com.torneios.model.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {
    boolean existsByNomeAndJogador(String nome, String jogador);
} 