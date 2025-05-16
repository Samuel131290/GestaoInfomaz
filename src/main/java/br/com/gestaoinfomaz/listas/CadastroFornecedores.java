package br.com.gestaoinfomaz.listas;
import br.com.gestaoinfomaz.classes.Fornecedor;
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
 * Classe responsável por carregar os dados da planilha Excel da aba "Cadastro Fornecedores"
 * e converter esses dados em uma lista de objetos do tipo Fornecedor.
 */
public class CadastroFornecedores {

    /**
     * Método que extrai os dados da aba "Cadastro Fornecedores" do arquivo Excel especificado e retorna
     * uma lista contendo os fornecedores cadastrados.
     *
     * @param caminhoArquivo caminho do arquivo Excel (.xlsx)
     * @return lista de objetos Fornecedor carregados da planilha
     */
    public List<Fornecedor> listaCadastroFornecedores(String caminhoArquivo) {
        List<Fornecedor> fornecedores = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(caminhoArquivo));
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Obtém a planilha "Cadastro Fornecedores"
            Sheet sheet = workbook.getSheet("Cadastro Fornecedores");
            if (sheet == null) {
                System.out.println("Aba 'Cadastro Fornecedores' não encontrada!");
                return fornecedores;
            }

            boolean pularLinhas = true;
            int linhasPuladas = 0;

            // Percorre as linhas da planilha, pulando as 2 primeiras (cabeçalhos)
            for (Row row : sheet) {
                if (pularLinhas) {
                    linhasPuladas++;
                    if (linhasPuladas < 3) {
                        continue; // ignora as linhas de cabeçalho
                    } else {
                        pularLinhas = false;
                    }
                }

                // Extrai os dados das células utilizando utilitário ExcelUtils
                // Coluna 0: ID do fornecedor (String, ex: "F100")
                String idFornecedor = ExcelUtils.getStringFromCell(row.getCell(0));
                String nomeFornecedor = ExcelUtils.getStringFromCell(row.getCell(1));
                Date dataCadastro = parseDateFromCell(row.getCell(2));

                // Adiciona o fornecedor à lista se o ID não estiver vazio
                if (!idFornecedor.isEmpty()) {
                    Fornecedor fornecedor = new Fornecedor(idFornecedor, nomeFornecedor, dataCadastro);
                    fornecedores.add(fornecedor);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fornecedores;
    }

    /**
     * Converte uma célula Excel em Date.
     * Suporta células do tipo numérico formatadas como data ou string no formato "dd/MM/yyyy".
     *
     * @param cell célula a ser convertida
     * @return objeto Date correspondente ou null se a conversão não for possível
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
                // Ignora erros de parse e retorna null
            }
        }
        return null;
    }
}
