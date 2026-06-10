package com.seuportifolio.controle_cabos.service;

import com.seuportifolio.controle_cabos.dto.ConsumoDTO;
import com.seuportifolio.controle_cabos.model.Equipe;
import com.seuportifolio.controle_cabos.model.ModeloCabo;
import com.seuportifolio.controle_cabos.model.MovimentacaoEstoque;
import com.seuportifolio.controle_cabos.model.Ocorrencia;
import com.seuportifolio.controle_cabos.model.TipoMovimentacao;
import com.seuportifolio.controle_cabos.repository.EquipeRepository;
import com.seuportifolio.controle_cabos.repository.MovimentacaoEstoqueRepository;
import com.seuportifolio.controle_cabos.repository.OcorrenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class EstoqueService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private OcorrenciaRepository ocorrenciaRepository;

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoRepository;

    @Transactional
    public void registrarConsumo(ConsumoDTO dto) {
        Equipe equipe = equipeRepository.findById(dto.getEquipeId())
                .orElseThrow(() -> new RuntimeException("Erro: Equipe não encontrada no sistema."));

        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setNumeroOs(dto.getNumeroOs());
        ocorrencia.setTipoServico(dto.getTipoServico());
        ocorrencia.setModeloCabo(dto.getModeloCabo());
        ocorrencia.setDataHora(LocalDateTime.now());
        ocorrencia.setEquipe(equipe);

        ocorrenciaRepository.save(ocorrencia);

        MovimentacaoEstoque saida = new MovimentacaoEstoque();
        saida.setEquipe(equipe);
        saida.setOcorrencia(ocorrencia);
        saida.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        saida.setModeloCabo(dto.getModeloCabo());
        saida.setQuantidadeMetros(dto.getQuantidadeMetros());
        saida.setDataRegistro(LocalDateTime.now());

        movimentacaoRepository.save(saida);
    }

    // Método da Auditoria posicionado corretamente
    public BigDecimal consultarGastoTotal(String matriculaLider, ModeloCabo modeloCabo) {
        return movimentacaoRepository.somarSaidasPorLiderECabo(matriculaLider, modeloCabo);
    }
    // Método para estornar um lançamento errado
    @Transactional
    public void realizarEstorno(com.seuportifolio.controle_cabos.dto.AjusteDTO dto) {

        // 1. Acha o lançamento original que foi feito errado
        MovimentacaoEstoque original = movimentacaoRepository.findById(dto.getMovimentacaoId())
                .orElseThrow(() -> new RuntimeException("Erro: Lançamento não encontrado."));

        // 2. Cria o lançamento de correção (Devolvendo o material pro caminhão)
        MovimentacaoEstoque estorno = new MovimentacaoEstoque();
        estorno.setEquipe(original.getEquipe());
        estorno.setOcorrencia(original.getOcorrencia()); // Mantém amarrado na mesma OS
        estorno.setModeloCabo(original.getModeloCabo());
        estorno.setQuantidadeMetros(original.getQuantidadeMetros()); // Devolve a mesma metragem

        // A MÁGICA: Em vez de SAÍDA, gera uma ENTRADA (Devolução)
        estorno.setTipoMovimentacao(TipoMovimentacao.ENTRADA);

        estorno.setJustificativa(dto.getJustificativa()); // Salva o motivo
        estorno.setMovimentacaoOrigem(original); // Amarramos o estorno ao erro original (Rastreabilidade)
        estorno.setDataRegistro(LocalDateTime.now());

        movimentacaoRepository.save(estorno);
    }
}