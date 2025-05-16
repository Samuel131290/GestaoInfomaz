package br.com.gestaoinfomaz.listas;
import br.com.gestaoinfomaz.classes.TransacaoVenda;
import br.com.gestaoinfomaz.excel.ExcelUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe responsável por carregar os dados da planilha Excel da aba "Transações Vendas"
 * e armazenar uma lista de objetos TransacaoVenda correspondentes.
 */
public class TransacoesVendas {

    // Lista que armazena todas as transações de venda carregadas da planilha
    private List<TransacaoVenda> transacoes = new ArrayList<>();

    /**
     * Construtor que recebe o caminho do arquivo Excel e dispara o carregamento dos dados.
     *
     * @param caminhoArquivo caminho do arquivo Excel (.xlsx) contendo a aba "Transações Vendas"
     */
    public TransacoesVendas(String caminhoArquivo) {
        carregarTransacoes(caminhoArquivo);
    }

    /**
     * Método que extrai os dados da aba "Transações Vendas" do arquivo Excel informado e retorna
     * uma lista contendo os transações realizadas.
     *
     * @param caminhoArquivo caminho do arquivo Excel (.xlsx)
     */
    private void carregarTransacoes(String caminhoArquivo) {
        try (FileInputStream fis = new FileInputStream(caminhoArquivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Transações Vendas");

            if (sheet == null) {
                System.out.println("Aba 'Transações Vendas' não encontrada!");
                return;
            }

            int primeiraLinhaDados = 1; // Ignora a linha 0 que contém o cabeçalho

            // Percorre as linhas da planilha, iniciando da linha 1
            for (int i = primeiraLinhaDados; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue; // Pula linhas vazias

                int idNota = ExcelUtils.getIntFromCell(row.getCell(0));
                Date dataNota = getDateFromCell(row.getCell(1));
                double valorNota = parseDoubleFromCell(row.getCell(2));
                double valorItem = parseDoubleFromCell(row.getCell(3));
                int qtdItem = ExcelUtils.getIntFromCell(row.getCell(4));
                int idProduto = ExcelUtils.getIntFromCell(row.getCell(5));
                int idCliente = ExcelUtils.getIntFromCell(row.getCell(6));

                // Adiciona somente se idNota for diferente de zero (evita linhas inválidas)
                if (idNota != 0) {
                    TransacaoVenda transacao = new TransacaoVenda(idNota, dataNota, valorNota,
                            valorItem, qtdItem, idProduto, idCliente);
                    transacoes.add(transacao);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converte o conteúdo da célula para objeto Date.
     * Suporta células com valor numérico formatado como data
     * ou string no formato "dd/MM/yyyy".
     *
     * @param cell célula a ser convertida
     * @return objeto Date ou null se não for possível converter
     */
    private Date getDateFromCell(Cell cell) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            }
        } else if (cell.getCellType() == CellType.STRING) {
            String dataStr = cell.getStringCellValue();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return sdf.parse(dataStr);
            } catch (ParseException e) {
                // Ignora erro de parse, pode ser célula inválida ou vazia
            }
        }
        return null;
    }

    /**
     * Converte o conteúdo da célula para valor double.
     * Suporta células numéricas ou texto com vírgula decimal e ponto como separador de milhar.
     *
     * @param cell célula a ser convertida
     * @return valor double ou 0 se inválido
     */
    private double parseDoubleFromCell(Cell cell) {
        if (cell == null) return 0;

        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            String valorStr = cell.getStringCellValue()
                    .replace(".", "")  // remove pontos separadores de milhar
                    .replace(",", "."); // substitui vírgula por ponto decimal
            try {
                return Double.parseDouble(valorStr);
            } catch (NumberFormatException e) {
                // Ignora erros e retorna 0
            }
        }
        return 0;
    }

    /**
     * Retorna a lista com todas as transações de venda carregadas.
     *
     * @return lista de objetos Transações Vendidas carregados da planilha
     */
    public List<TransacaoVenda> getTransacoes() {
        return transacoes;
    }
}
