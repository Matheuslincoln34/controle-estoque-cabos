package com.seuportifolio.controle_cabos.dto;

import com.seuportifolio.controle_cabos.model.ModeloCabo;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AbastecimentoDTO {
    // Para qual equipe o material está indo
    private Long equipeId;

    // Qual é o tipo de cabo
    private ModeloCabo modeloCabo;

    // Quantos metros a bobina tem
    private BigDecimal quantidadeMetros;
}