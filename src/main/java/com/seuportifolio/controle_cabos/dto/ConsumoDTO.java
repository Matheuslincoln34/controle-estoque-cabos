package com.seuportifolio.controle_cabos.dto;

import com.seuportifolio.controle_cabos.model.ModeloCabo;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ConsumoDTO {

    private Long equipeId;
    private String numeroOs;
    private BigDecimal quantidadeMetros;

    // Novos campos para a realidade de ramais e redes
    private String tipoServico; // Ex: "Substituição de Ramal", "Reparo na Rede"
    private ModeloCabo modeloCabo;  // Ex: O modelo exato do cabo especial

}