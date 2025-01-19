package com.torneios.service.impl;

import com.torneios.model.*;
import com.torneios.model.enums.TipoFase;
import com.torneios.model.enums.StatusPartida;
import com.torneios.repository.FaseRepository;
import com.torneios.repository.PartidaRepository;
import com.torneios.service.FaseService;
import com.torneios.service.CampeonatoService;
import com.torneios.service.InscricaoService;
import com.torneios.exception.NegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaseServiceImpl implements FaseService {

    private final FaseRepository faseRepository;
    private final PartidaRepository partidaRepository;
    private final CampeonatoService campeonatoService;
    private final InscricaoService inscricaoService;

    @Override
    @Transactional
    public Fase criarFaseGrupos(Long campeonatoId) {
        Campeonato campeonato = campeonatoService.buscarPorId(campeonatoId);
        List<Fase> fases = faseRepository.findByCampeonato(campeonato);
        
        if (!fases.isEmpty()) {
            throw new NegocioException("Campeonato já possui fases criadas");
        }

        Fase fase = new Fase();
        fase.setCampeonato(campeonato);
        fase.setTipo(TipoFase.GRUPOS);
        fase.setNumero(1);

        return faseRepository.save(fase);
    }

    @Override
    @Transactional
    public Fase criarProximaFase(Long campeonatoId) {
        Campeonato campeonato = campeonatoService.buscarPorId(campeonatoId);
        List<Fase> fases = faseRepository.findByCampeonatoOrderByNumeroAsc(campeonato);
        
        if (fases.isEmpty()) {
            throw new NegocioException("Fase de grupos ainda não foi criada");
        }

        Fase ultimaFase = fases.get(fases.size() - 1);
        verificarPartidasFinalizadas(ultimaFase);

        TipoFase proximoTipo = determinarProximaFase(ultimaFase.getTipo());
        if (proximoTipo == null) {
            throw new NegocioException("Não há próxima fase disponível");
        }

        Fase novaFase = new Fase();
        novaFase.setCampeonato(campeonato);
        novaFase.setTipo(proximoTipo);
        novaFase.setNumero(ultimaFase.getNumero() + 1);

        return faseRepository.save(novaFase);
    }

    @Override
    public List<Fase> listarPorCampeonato(Long campeonatoId) {
        Campeonato campeonato = campeonatoService.buscarPorId(campeonatoId);
        return faseRepository.findByCampeonatoOrderByNumeroAsc(campeonato);
    }

    @Override
    @Transactional
    public void gerarPartidas(Long faseId) {
        Fase fase = faseRepository.findById(faseId)
                .orElseThrow(() -> new NegocioException("Fase não encontrada"));

        if (!partidaRepository.findByFase(fase).isEmpty()) {
            throw new NegocioException("Partidas já foram geradas para esta fase");
        }

        List<Time> times = obterTimesParaFase(fase);
        if (fase.getTipo() == TipoFase.GRUPOS) {
            gerarPartidasGrupo(fase, times);
        } else {
            gerarPartidasEliminatorias(fase, times);
        }
    }

    private void verificarPartidasFinalizadas(Fase fase) {
        List<Partida> partidas = partidaRepository.findByFase(fase);
        boolean todasFinalizadas = partidas.stream()
                .allMatch(p -> p.getStatus() == StatusPartida.FINALIZADA);
                
        if (!todasFinalizadas) {
            throw new NegocioException("Existem partidas não finalizadas na fase atual");
        }
    }

    private TipoFase determinarProximaFase(TipoFase faseAtual) {
        switch (faseAtual) {
            case GRUPOS:
                return TipoFase.OITAVAS;
            case OITAVAS:
                return TipoFase.QUARTAS;
            case QUARTAS:
                return TipoFase.SEMIFINAL;
            case SEMIFINAL:
                return TipoFase.FINAL;
            default:
                return null;
        }
    }

    private List<Time> obterTimesParaFase(Fase fase) {
        if (fase.getTipo() == TipoFase.GRUPOS) {
            return inscricaoService.listarInscricoesAprovadas(fase.getCampeonato().getId())
                    .stream()
                    .map(Inscricao::getTime)
                    .collect(Collectors.toList());
        }

        // Para fases eliminatórias, obtém os classificados da fase anterior
        Fase faseAnterior = faseRepository.findByCampeonatoOrderByNumeroAsc(fase.getCampeonato())
                .stream()
                .filter(f -> f.getNumero() == fase.getNumero() - 1)
                .findFirst()
                .orElseThrow(() -> new NegocioException("Fase anterior não encontrada"));

        return obterClassificados(faseAnterior);
    }

    private void gerarPartidasGrupo(Fase fase, List<Time> times) {
        if (times.size() < fase.getCampeonato().getNumeroGrupos() * fase.getCampeonato().getTimesPorGrupo()) {
            throw new NegocioException("Número insuficiente de times para gerar os grupos");
        }

        // Embaralha os times para distribuição aleatória
        List<Time> timesEmbaralhados = new ArrayList<>(times);
        Collections.shuffle(timesEmbaralhados);

        // Divide os times em grupos
        int timesPorGrupo = fase.getCampeonato().getTimesPorGrupo();
        List<List<Time>> grupos = new ArrayList<>();
        
        for (int i = 0; i < fase.getCampeonato().getNumeroGrupos(); i++) {
            int inicio = i * timesPorGrupo;
            int fim = inicio + timesPorGrupo;
            grupos.add(timesEmbaralhados.subList(inicio, fim));
        }

        // Gera partidas para cada grupo
        LocalDateTime dataInicial = fase.getCampeonato().getDataInicio();
        
        for (int numeroGrupo = 0; numeroGrupo < grupos.size(); numeroGrupo++) {
            List<Time> timesDoGrupo = grupos.get(numeroGrupo);
            
            // Gera todas as combinações possíveis de jogos (todos contra todos)
            for (int i = 0; i < timesDoGrupo.size(); i++) {
                for (int j = i + 1; j < timesDoGrupo.size(); j++) {
                    Partida partida = new Partida();
                    partida.setFase(fase);
                    partida.setTimeCasa(timesDoGrupo.get(i));
                    partida.setTimeVisitante(timesDoGrupo.get(j));
                    partida.setDataHora(dataInicial.plusDays(numeroGrupo));
                    partida.setStatus(StatusPartida.AGENDADA);
                    
                    partidaRepository.save(partida);
                }
            }
        }
    }

    private void gerarPartidasEliminatorias(Fase fase, List<Time> times) {
        int numeroPartidas = determinarNumeroPartidas(fase.getTipo());
        if (times.size() != numeroPartidas * 2) {
            throw new NegocioException("Número incorreto de times para a fase " + fase.getTipo());
        }

        LocalDateTime dataInicial = fase.getCampeonato().getDataInicio();
        
        for (int i = 0; i < times.size(); i += 2) {
            Partida partida = new Partida();
            partida.setFase(fase);
            partida.setTimeCasa(times.get(i));
            partida.setTimeVisitante(times.get(i + 1));
            partida.setDataHora(dataInicial.plusDays(fase.getNumero()));
            partida.setStatus(StatusPartida.AGENDADA);
            
            partidaRepository.save(partida);
        }
    }

    private List<Time> obterClassificados(Fase fase) {
        List<Partida> partidas = partidaRepository.findByFase(fase);
        
        if (fase.getTipo() == TipoFase.GRUPOS) {
            return obterClassificadosGrupos(fase, partidas);
        } else {
            return obterClassificadosEliminatorias(partidas);
        }
    }

    private List<Time> obterClassificadosGrupos(Fase fase, List<Partida> partidas) {
        // Agrupa as partidas por grupo (usando o primeiro time como referência do grupo)
        Map<Time, List<Partida>> partidasPorGrupo = new HashMap<>();
        
        for (Partida partida : partidas) {
            partidasPorGrupo.computeIfAbsent(partida.getTimeCasa(), k -> new ArrayList<>()).add(partida);
            partidasPorGrupo.computeIfAbsent(partida.getTimeVisitante(), k -> new ArrayList<>()).add(partida);
        }

        // Calcula a classificação para cada grupo
        Map<Time, ClassificacaoTime> classificacaoGeral = new HashMap<>();
        
        for (Map.Entry<Time, List<Partida>> entry : partidasPorGrupo.entrySet()) {
            Time time = entry.getKey();
            List<Partida> partidasDoTime = entry.getValue();
            
            ClassificacaoTime classificacao = new ClassificacaoTime(time);
            for (Partida partida : partidasDoTime) {
                if (partida.getStatus() == StatusPartida.FINALIZADA) {
                    classificacao.computarPartida(partida);
                }
            }
            classificacaoGeral.put(time, classificacao);
        }

        // Ordena e retorna os classificados
        return classificacaoGeral.values().stream()
                .sorted()
                .limit(determinarNumeroClassificados(fase.getTipo()))
                .map(ClassificacaoTime::getTime)
                .collect(Collectors.toList());
    }

    private List<Time> obterClassificadosEliminatorias(List<Partida> partidas) {
        return partidas.stream()
                .filter(p -> p.getStatus() == StatusPartida.FINALIZADA)
                .map(this::obterVencedor)
                .collect(Collectors.toList());
    }

    private Time obterVencedor(Partida partida) {
        if (partida.getGolsTimeCasa() > partida.getGolsTimeVisitante()) {
            return partida.getTimeCasa();
        }
        return partida.getTimeVisitante();
    }

    private int determinarNumeroPartidas(TipoFase tipo) {
        switch (tipo) {
            case OITAVAS:
                return 8;
            case QUARTAS:
                return 4;
            case SEMIFINAL:
                return 2;
            case FINAL:
                return 1;
            default:
                throw new NegocioException("Tipo de fase inválido para eliminatórias");
        }
    }

    private int determinarNumeroClassificados(TipoFase tipo) {
        if (tipo == TipoFase.GRUPOS) {
            return 16; // Classificados para as oitavas (2 por grupo, considerando 8 grupos)
        }
        throw new NegocioException("Tipo de fase inválido para classificação");
    }
} 