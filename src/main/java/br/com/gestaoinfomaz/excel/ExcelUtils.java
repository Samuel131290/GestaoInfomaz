package br.com.gestaoinfomaz.excel;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Classe utilitária para manipulação de células de planilhas Excel com Apache
 * POI. Fornece métodos para extrair valores inteiros e strings de células,
 * tratando diferentes tipos de dados e casos nulos.
 */
public class ExcelUtils {

    /**
     * Converte o conteúdo de uma célula do Excel em um número inteiro. Se a
     * célula for nula, contiver um texto não numérico ou for de tipo
     * incompatível, o método retorna 0.
     *
     * @param cell Célula a ser lida
     * @return Valor inteiro contido na célula ou 0 em caso de erro
     */
    public static int getIntFromCell(Cell cell) {
        if (cell == null) {
            return 0;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                return Integer.parseInt(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return 0; // Retorna 0 se o conteúdo da célula não for um número válido
            }
            default:
                return 0;
        }
    }

    /**
     * Converte o conteúdo de uma célula do Excel em uma string. Trata
     * diferentes tipos de célula como STRING, NUMERIC, BOOLEAN, FORMULA e
     * BLANK. Células nulas ou em branco retornam uma string vazia.
     *
     * @param cell Célula a ser lida
     * @return Valor da célula em formato String
     */
    public static String getStringFromCell(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            default:
                return "";
        }
    }
}
