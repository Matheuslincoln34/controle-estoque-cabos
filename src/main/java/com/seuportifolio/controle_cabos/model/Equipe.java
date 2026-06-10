package com.seuportifolio.controle_cabos.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "equipes")
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeEquipe; // Ex: "Walmir x Wanderson"

    // --- Dados do Líder (O Responsável pela Carga e Auditoria) ---
    @Column(nullable = false)
    private String nomeLider;

    @Column(nullable = false, unique = true)
    private String matriculaLider; // A chave principal de rastreio no almoxarifado

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CargoOperacional cargoLider; // Ex: BTC-2

    // --- Dados do Ajudante ---
    @Column(nullable = false)
    private String nomeAjudante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CargoOperacional cargoAjudante; // Ex: BTC-1

    private boolean ativo = true;
}