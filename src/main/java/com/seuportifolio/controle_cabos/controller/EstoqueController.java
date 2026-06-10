package com.seuportifolio.controle_cabos.controller;

import com.seuportifolio.controle_cabos.dto.ConsumoDTO;
import com.seuportifolio.controle_cabos.model.ModeloCabo;
import com.seuportifolio.controle_cabos.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PostMapping("/consumo")
    public ResponseEntity<Void> lancarConsumo(@RequestBody ConsumoDTO dto) {
        estoqueService.registrarConsumo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Rota da Auditoria posicionada corretamente
    @GetMapping("/auditoria/gasto")
    public ResponseEntity<BigDecimal> consultarGasto(
            @RequestParam String matricula,
            @RequestParam ModeloCabo cabo) {

        BigDecimal totalGasto = estoqueService.consultarGastoTotal(matricula, cabo);
        return ResponseEntity.ok(totalGasto);
    }
    // Rota POST: Usada quando o supervisor autoriza o estorno de um erro
    @PostMapping("/estorno")
    public ResponseEntity<Void> estornarConsumo(@RequestBody com.seuportifolio.controle_cabos.dto.AjusteDTO dto) {
        estoqueService.realizarEstorno(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}