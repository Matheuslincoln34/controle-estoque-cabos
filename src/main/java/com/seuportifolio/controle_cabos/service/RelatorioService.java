package com.seuportifolio.controle_cabos.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.seuportifolio.controle_cabos.model.MovimentacaoEstoque;
import com.seuportifolio.controle_cabos.repository.MovimentacaoEstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoRepository;

    public byte[] gerarRelatorioTurno(String matriculaLider) {
        List<MovimentacaoEstoque> historico = movimentacaoRepository.findByEquipeMatriculaLiderOrderByDataRegistroDesc(matriculaLider);

        if (historico.isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Nenhum histórico encontrado para esta matrícula.");
        }

        String nomeEquipe = historico.get(0).getEquipe().getNomeEquipe();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, out);

        document.open();

        // 1. Título Principal
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
        Paragraph titulo = new Paragraph("RELATÓRIO DE FECHAMENTO DE TURNO", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        // 2. Metadados da Equipe
        Font fontSub = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
        document.add(new Paragraph("Equipe Operacional: " + nomeEquipe, fontSub));
        document.add(new Paragraph("Matrícula do Líder: " + matriculaLider, fontSub));
        document.add(new Paragraph("Status de Auditoria: SISTEMA CONSOLIDADO", fontSub));

        Paragraph linhaSeparadora = new Paragraph("----------------------------------------------------------------------------------------------------", fontSub);
        linhaSeparadora.setSpacingAfter(15);
        document.add(linhaSeparadora);

        // 3. Criação da Tabela de Movimentações
        PdfPTable tabela = new PdfPTable(4); // 4 Colunas
        tabela.setWidthPercentage(100);
        tabela.setSpacingBefore(10);

        // Configuração do Cabeçalho da Tabela
        Font fontCabecalho = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
        Color corFundoCabecalho = new Color(41, 128, 185); // Azul Corporativo

        String[] colunas = {"Data/Hora", "Operação", "Material", "Metragem"};
        for (String coluna : colunas) {
            PdfPCell cell = new PdfPCell(new Paragraph(coluna, fontCabecalho));
            cell.setBackgroundColor(corFundoCabecalho);
            cell.setPadding(6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabela.addCell(cell);
        }

        // Preenchimento dos dados do Banco na Tabela do PDF
        Font fontDados = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (MovimentacaoEstoque mov : historico) {
            tabela.addCell(new PdfPCell(new Paragraph(mov.getDataRegistro().format(formatter), fontDados)));
            tabela.addCell(new PdfPCell(new Paragraph(mov.getTipoMovimentacao().toString(), fontDados)));
            tabela.addCell(new PdfPCell(new Paragraph(mov.getModeloCabo().toString(), fontDados)));

            PdfPCell cellMetros = new PdfPCell(new Paragraph(mov.getQuantidadeMetros().toString() + " m", fontDados));
            cellMetros.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabela.addCell(cellMetros);
        }

        document.add(tabela);
        document.close();

        return out.toByteArray();
    }
}