package com.seuportifolio.controle_cabos.repository;

import com.seuportifolio.controle_cabos.model.ModeloCabo;
import com.seuportifolio.controle_cabos.model.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    // Agora o banco entende a diferença entre gasto real e material devolvido!
    @Query("SELECT COALESCE(SUM(CASE " +
            "WHEN m.tipoMovimentacao = 'SAIDA' THEN m.quantidadeMetros " +
            "WHEN m.tipoMovimentacao = 'ENTRADA' THEN -m.quantidadeMetros " +
            "ELSE 0 END), 0) " +
            "FROM MovimentacaoEstoque m " +
            "WHERE m.equipe.matriculaLider = :matricula " +
            "AND m.modeloCabo = :modeloCabo")
    BigDecimal somarSaidasPorLiderECabo(@Param("matricula") String matricula,
                                        @Param("modeloCabo") ModeloCabo modeloCabo);
    // A sua fórmula exata: (Abastecimento + Estorno) - (Saída) = Saldo do Caminhão
    @Query("SELECT COALESCE(SUM(CASE " +
            "WHEN m.tipoMovimentacao = 'ABASTECIMENTO' THEN m.quantidadeMetros " +
            "WHEN m.tipoMovimentacao = 'ENTRADA' THEN m.quantidadeMetros " +
            "WHEN m.tipoMovimentacao = 'SAIDA' THEN -m.quantidadeMetros " +
            "ELSE 0 END), 0) " +
            "FROM MovimentacaoEstoque m " +
            "WHERE m.equipe.matriculaLider = :matricula " +
            "AND m.modeloCabo = :modeloCabo")
    BigDecimal calcularSaldoAtualPorLiderECabo(@Param("matricula") String matricula,
                                               @Param("modeloCabo") ModeloCabo modeloCabo);
    // Busca todo o histórico de movimentações de uma equipe filtrando pela matrícula do líder
    List<MovimentacaoEstoque> findByEquipeMatriculaLiderOrderByDataRegistroDesc(String matriculaLider);

}