package com.seuportifolio.controle_cabos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ocorrencias")
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroOs;

    // Novas colunas mapeadas no banco de dados
    @Column(nullable = false)
    private String tipoServico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModeloCabo modeloCabo;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "equipe_id", nullable = false)
    private Equipe equipe;
}