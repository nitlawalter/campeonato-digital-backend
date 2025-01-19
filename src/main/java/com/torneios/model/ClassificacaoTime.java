package com.torneios.model;

import lombok.Data;

@Data
public class ClassificacaoTime implements Comparable<ClassificacaoTime> {
    private Time time;
    private int pontos;
    private int jogos;
    private int vitorias;
    private int empates;
    private int derrotas;
    private int golsPro;
    private int golsContra;
    private int saldoGols;

    public ClassificacaoTime(Time time) {
        this.time = time;
    }

    public void computarPartida(Partida partida) {
        jogos++;
        
        boolean ehTimeCasa = partida.getTimeCasa().equals(time);
        int golsFeitos = ehTimeCasa ? partida.getGolsTimeCasa() : partida.getGolsTimeVisitante();
        int golsSofridos = ehTimeCasa ? partida.getGolsTimeVisitante() : partida.getGolsTimeCasa();

        golsPro += golsFeitos;
        golsContra += golsSofridos;
        saldoGols = golsPro - golsContra;

        if (golsFeitos > golsSofridos) {
            vitorias++;
            pontos += 3;
        } else if (golsFeitos == golsSofridos) {
            empates++;
            pontos += 1;
        } else {
            derrotas++;
        }
    }

    @Override
    public int compareTo(ClassificacaoTime outro) {
        // Ordenação por pontos, saldo de gols e gols pró
        if (this.pontos != outro.pontos) {
            return Integer.compare(outro.pontos, this.pontos);
        }
        if (this.saldoGols != outro.saldoGols) {
            return Integer.compare(outro.saldoGols, this.saldoGols);
        }
        return Integer.compare(outro.golsPro, this.golsPro);
    }
} 