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

    // 1. Método com a nova TRAVA de Saldo Negativo
    @Transactional
    public void registrarConsumo(ConsumoDTO dto) {
        Equipe equipe = equipeRepository.findById(dto.getEquipeId())
                .orElseThrow(() -> new RuntimeException("Erro: Equipe não encontrada no sistema."));

        BigDecimal saldoAtual = movimentacaoRepository.calcularSaldoAtualPorLiderECabo(
                equipe.getMatriculaLider(), dto.getModeloCabo());

        if (dto.getQuantidadeMetros().compareTo(saldoAtual) > 0) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Saldo insuficiente no caminhão. Saldo atual do " + dto.getModeloCabo() + ": " + saldoAtual + " metros.");
        }

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

    // 2. Método de Auditoria de Gastos
    public BigDecimal consultarGastoTotal(String matriculaLider, ModeloCabo modeloCabo) {
        return movimentacaoRepository.somarSaidasPorLiderECabo(matriculaLider, modeloCabo);
    }

    // 3. Método de Correção/Estorno
    @Transactional
    public void realizarEstorno(com.seuportifolio.controle_cabos.dto.AjusteDTO dto) {
        MovimentacaoEstoque original = movimentacaoRepository.findById(dto.getMovimentacaoId())
                .orElseThrow(() -> new RuntimeException("Erro: Lançamento não encontrado."));

        MovimentacaoEstoque estorno = new MovimentacaoEstoque();
        estorno.setEquipe(original.getEquipe());
        estorno.setOcorrencia(original.getOcorrencia());
        estorno.setModeloCabo(original.getModeloCabo());
        estorno.setQuantidadeMetros(original.getQuantidadeMetros());
        estorno.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        estorno.setJustificativa(dto.getJustificativa());
        estorno.setMovimentacaoOrigem(original);
        estorno.setDataRegistro(LocalDateTime.now());

        movimentacaoRepository.save(estorno);
    }

    // 4. Método de Auditoria do Saldo Real
    public BigDecimal consultarSaldoAtual(String matriculaLider, ModeloCabo modeloCabo) {
        return movimentacaoRepository.calcularSaldoAtualPorLiderECabo(matriculaLider, modeloCabo);
    }

    // 5. Método de Abastecimento do Galpão para o Caminhão
    @Transactional
    public void registrarAbastecimento(com.seuportifolio.controle_cabos.dto.AbastecimentoDTO dto) {
        Equipe equipe = equipeRepository.findById(dto.getEquipeId())
                .orElseThrow(() -> new RuntimeException("Erro: Equipe não encontrada no sistema."));

        MovimentacaoEstoque abastecimento = new MovimentacaoEstoque();
        abastecimento.setEquipe(equipe);
        abastecimento.setTipoMovimentacao(TipoMovimentacao.ABASTECIMENTO);
        abastecimento.setModeloCabo(dto.getModeloCabo());
        abastecimento.setQuantidadeMetros(dto.getQuantidadeMetros());
        abastecimento.setDataRegistro(LocalDateTime.now());

        movimentacaoRepository.save(abastecimento);
    }
}