package br.com.gestaoinfomaz.listas;
import br.com.gestaoinfomaz.classes.Estoque;
import br.com.gestaoinfomaz.excel.ExcelUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe responsável por carregar os dados da planilha Excel da aba "Cadastro de Estoque"
 * e converter esses dados em uma lista de objetos do tipo Estoque.
 */
public class CadastroEstoque {

    /**
     * Método que extrai os dados da aba "Cadastro de Estoque" do arquivo Excel especificado e retorna
     * uma lista com os dados de estoque.
     *
     * @param caminhoArquivo caminho do arquivo Excel (.xlsx)
     * @return lista de objetos Estoque carregados da planilha
     */
    public List<Estoque> listaCadastroEstoque(String caminhoArquivo) {
        List<Estoque> estoques = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(caminhoArquivo));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Cadastro de Estoque");
            if (sheet == null) {
                System.out.println("Aba 'Cadastro de Estoque' não encontrada!");
                return estoques;
            }

            boolean pularLinhas = true;
            int linhasPuladas = 0;

            // Pula as 2 primeiras linhas (cabeçalhos)
            for (Row row : sheet) {
                if (pularLinhas) {
                    linhasPuladas++;
                    if (linhasPuladas < 3) {
                        continue;
                    } else {
                        pularLinhas = false;
                    }
                }

                int idEstoque = ExcelUtils.getIntFromCell(row.getCell(0));
                double valorEstoque = parseDoubleFromCell(row.getCell(1));
                int qtdEstoque = ExcelUtils.getIntFromCell(row.getCell(2));
                Date dataEstoque = parseDateFromCell(row.getCell(3));
                String idFornecedor = ExcelUtils.getStringFromCell(row.getCell(4));

                // Adiciona só se ID do estoque for diferente de zero
                if (idEstoque != 0) {
                    Estoque estoque = new Estoque(idEstoque, valorEstoque, qtdEstoque, dataEstoque, idFornecedor);
                    estoques.add(estoque);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return estoques;
    }

    /**
     * Converte uma célula Excel em Date.
     * Suporta células do tipo numérico formatadas como data ou string no formato "dd/MM/yyyy".
     *
     * @param cell célula a ser convertida
     * @return objeto Date ou null se inválido
     */
    private Date parseDateFromCell(Cell cell) {
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
                // Ignora erros de parse
            }
        }
        return null;
    }

    /**
     * Converte uma célula Excel em double.
     * Suporta número em formato numérico ou string com vírgula decimal e ponto como separador de milhar.
     *
     * @param cell célula a ser convertida
     * @return valor double ou 0 em caso de erro ou célula nula
     */
    private double parseDoubleFromCell(Cell cell) {
        if (cell == null) return 0;

        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            String valorStr = cell.getStringCellValue()
                    .replace(".", "")  // remover separadores de milhares
                    .replace(",", "."); // substituir vírgula por ponto decimal
            try {
                return Double.parseDouble(valorStr);
            } catch (NumberFormatException e) {
                // Ignora erros de parse
            }
        }
        return 0;
    }
}
