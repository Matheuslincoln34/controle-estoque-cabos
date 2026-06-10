package com.seuportifolio.controle_cabos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "movimentacoes_estoque")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "equipe_id", nullable = false)
    private com.seuportifolio.controle_cabos.model.Equipe equipe;

    @ManyToOne
    @JoinColumn(name = "ocorrencia_id")
    private com.seuportifolio.controle_cabos.model.Ocorrencia ocorrencia; // Pode ser nulo se for apenas um abastecimento do caminhão cesto no almoxarifado

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.seuportifolio.controle_cabos.model.TipoMovimentacao tipoMovimentacao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeMetros; // Usamos BigDecimal no Java para lidar com precisão e evitar erros de arredondamento

    @Column(nullable = false)
    private LocalDateTime dataRegistro;

    // --- Campos Exclusivos para Auditoria e Ajustes ---

    @Column(length = 500)
    private String justificativa;

    @ManyToOne
    @JoinColumn(name = "usuario_responsavel_id")
    private com.seuportifolio.controle_cabos.model.Usuario usuarioResponsavel;

    @ManyToOne
    @JoinColumn(name = "movimentacao_origem_id")
    private MovimentacaoEstoque movimentacaoOrigem; // Vincula o ajuste ao erro original
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModeloCabo modeloCabo; // Agora o sistema sabe EXATAMENTE qual cabo entrou ou saiu
}