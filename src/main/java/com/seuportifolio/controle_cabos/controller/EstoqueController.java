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
    @Autowired
    private com.seuportifolio.controle_cabos.service.RelatorioService relatorioService;

    @PostMapping("/consumo")
    public ResponseEntity<Void> lancarConsumo(@RequestBody ConsumoDTO dto) {
        estoqueService.registrarConsumo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Rota da Auditoria posicionada corretamente
    @GetMapping(value = "/auditoria/relatorio", produces = org.springframework.http.MediaType.APPLICATION_PDF_VALUE)
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
    // Rota GET: Mostra o saldo real que deve estar fisicamente na caçamba do caminhão
    @GetMapping("/auditoria/saldo")
    public ResponseEntity<BigDecimal> consultarSaldoAtual(
            @RequestParam String matricula,
            @RequestParam ModeloCabo cabo) {

        BigDecimal saldo = estoqueService.consultarSaldoAtual(matricula, cabo);
        return ResponseEntity.ok(saldo);
    }
    // Rota POST: Usada pelo Almoxarife para enviar as bobinas para a equipe
    // Rota POST: Usada pelo Almoxarife para enviar as bobinas para a equipe
    @PostMapping("/abastecimento")
    public ResponseEntity<Void> abastecerEquipe(@RequestBody com.seuportifolio.controle_cabos.dto.AbastecimentoDTO dto) {
        estoqueService.registrarAbastecimento(dto);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).build();
    } // <-- ESSA É A CHAVE QUE ESTAVA FALTANDO!

    // Rota GET: Gera e faz o download automático do PDF do turno
    @GetMapping("/auditoria/relatorio")
    public ResponseEntity<byte[]> baixarRelatorioTurno(@RequestParam String matricula) {
        byte[] pdfBytes = relatorioService.gerarRelatorioTurno(matricula);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio-turno-" + matricula + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, org.springframework.http.HttpStatus.OK);

        }
    }
