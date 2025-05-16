package br.com.gestaoinfomaz.listas;
import br.com.gestaoinfomaz.classes.Cliente;
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
 * Classe responsável por carregar os dados da planilha Excel da aba "Cadastro Clientes"
 * e converter esses dados em uma lista de objetos do tipo Cliente.
 */
public class CadastroClientes {

    /**
     * Método que extrai os dados da aba "Cadastro Clientes" do arquivo Excel especificado e retorna
     * uma lista contendo os clientes cadastrados.
     *
     * @param caminhoArquivo caminho do arquivo Excel (.xlsx)
     * @return lista de clientes carregados da planilha
     */
    public List<Cliente> listaClientes(String caminhoArquivo) {
        List<Cliente> clientes = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(caminhoArquivo));
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Obtém a planilha "Cadastro Clientes"
            Sheet sheet = workbook.getSheet("Cadastro Clientes");
            if (sheet == null) {
                System.out.println("Aba 'Cadastro Clientes' não encontrada!");
                return clientes;
            }

            boolean pularLinhas = true;
            int linhasPuladas = 0;
            for (Row row : sheet) {
                // Pula as duas primeiras linhas (cabeçalhos ou dados irrelevantes)
                if (pularLinhas) {
                    linhasPuladas++;
                    if (linhasPuladas < 3) {
                        continue;
                    } else {
                        pularLinhas = false;
                    }
                }

                // Lê as células da linha com auxílio do ExcelUtils
                int idCliente = ExcelUtils.getIntFromCell(row.getCell(0));
                String nomeCliente = ExcelUtils.getStringFromCell(row.getCell(1));
                Date dataCadastro = parseDateFromCell(row.getCell(2));

                // Somente adiciona clientes com ID válido (não zero)
                if (idCliente != 0) {
                    Cliente cliente = new Cliente(idCliente, nomeCliente, dataCadastro);
                    clientes.add(cliente);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    /**
     * Converte uma célula do Excel para objeto Date.
     * Suporta células do tipo data/número ou string com formato "dd/MM/yyyy".
     *
     * @param cell célula contendo a data
     * @return objeto Date ou null se a conversão falhar ou célula for nula
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
                // Ignora erro na data, pode ser célula vazia ou inválida
            }
        }
        return null;
    }
}
