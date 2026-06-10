package com.seuportifolio.controle_cabos.model;

public enum CargoOperacional {

    BTC_1("Ajudante / BTC-1"),
    BTC_2("Líder de Turma / BTC-2");

    private final String descricao;

    CargoOperacional(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}