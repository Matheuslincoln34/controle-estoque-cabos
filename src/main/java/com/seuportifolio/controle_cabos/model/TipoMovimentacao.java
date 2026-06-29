package com.seuportifolio.controle_cabos.model;

public enum TipoMovimentacao {
    ENTRADA, // Recebeu cabo do almoxarifado
    SAIDA,   // Gastou cabo na ocorrência
    AJUSTE,   // Correção feita pelo supervisor
    ABASTECIMENTO
}