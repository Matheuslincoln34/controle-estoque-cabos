package com.seuportifolio.controle_cabos.dto;

import lombok.Data;

@Data
public class AjusteDTO {
    // O ID da movimentação que a equipe digitou errado
    private Long movimentacaoId;

    // O motivo obrigatório para a auditoria aceitar o estorno
    private String justificativa;
}