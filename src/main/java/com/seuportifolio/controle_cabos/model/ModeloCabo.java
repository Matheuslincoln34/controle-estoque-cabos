package com.seuportifolio.controle_cabos.model;

public enum ModeloCabo {

    CABO_DUPLEX_6("324675", "Cabo Duplex 6"),
    CABO_QPX_4("324695", "Cabo QPX 4"),
    CABO_QPX_6("324689", "Cabo QPX 6"),
    CABO_TRIPLEX_6("324685", "Cabo TRIPLEX 6"),
    CABO_70_ESPECIAL("324605", "Cabo 70 (especial)"),
    CABO_XLPE_16("324793", "Cabo XLPE 16mm² (especial)"),
    CABO_185_ESPECIAL("324604", "Cabo 185 (especial)"),
    CABO_PVC_1_0("324800", "Cabo PVC 1/0 (especial)");

    private final String codigoSAP;
    private final String descricao;

    // Construtor do Enum
    ModeloCabo(String codigoSAP, String descricao) {
        this.codigoSAP = codigoSAP;
        this.descricao = descricao;
    }

    public String getCodigoSAP() {
        return codigoSAP;
    }

    public String getDescricao() {
        return descricao;
    }
}