package br.com.gestaoinfomaz.listas;
import br.com.gestaoinfomaz.classes.Produto;
import br.com.gestaoinfomaz.excel.ExcelUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Classe responsável por carregar os dados da planilha Excel da aba "Cadastro Produtos"
 * e converter esses dados em uma lista de objetos do tipo Produto.
 */
public class CadastroProdutos {

    /**
     * Método que extrai os dados da aba "Cadastro Produtos" do arquivo Excel informado e retorna
     * uma lista contendo os produtos cadastrados.
     *
     * @param caminhoArquivo caminho do arquivo Excel (.xlsx)
     * @return lista de objetos Produto carregados da planilha
     */
    public List<Produto> listaCadastroProdutos(String caminhoArquivo) {
        List<Produto> produtos = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(caminhoArquivo));
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            // Obtém a planilha "Cadastro Produtos"
            Sheet sheet = workbook.getSheet("Cadastro Produtos");
            if (sheet == null) {
                System.out.println("Aba 'Cadastro Produtos' não encontrada!");
                return produtos;
            }

            boolean pularLinhas = true;
            int linhasPuladas = 0;

            // Itera pelas linhas da planilha, pulando as 2 primeiras (cabeçalho)
            for (Row row : sheet) {
                if (pularLinhas) {
                    linhasPuladas++;
                    if (linhasPuladas < 3) {
                        continue; // ignora as duas primeiras linhas
                    } else {
                        pularLinhas = false;
                    }
                }

                // Extrai dados das células usando métodos utilitários
                int idProduto = ExcelUtils.getIntFromCell(row.getCell(0));
                int idEstoque = ExcelUtils.getIntFromCell(row.getCell(1));
                String nomeProduto = ExcelUtils.getStringFromCell(row.getCell(2));
                String categoria = ExcelUtils.getStringFromCell(row.getCell(3));

                // Cria um objeto Produto e adiciona à lista
                Produto produto = new Produto(idProduto, idEstoque, nomeProduto, categoria);
                produtos.add(produto);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return produtos;
    }
}
