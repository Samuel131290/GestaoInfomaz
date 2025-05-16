package br.com.gestaoinfomaz.infomaz;
import static br.com.gestaoinfomaz.infomaz.Main.centralizarTexto;
import br.com.gestaoinfomaz.classes.Estoque;
import br.com.gestaoinfomaz.classes.Fornecedor;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe responsável por gerar um ranking mensal dos fornecedores
 * baseado na quantidade de itens que cada um forneceu ao estoque.
 */
public class RankingFornecedores {

    /**
     * Exibe o ranking mensal de fornecedores, mostrando a quantidade total de itens fornecidos por mês.
     *
     * @param estoques Lista de registros de estoque (inclui quantidade e data de entrada).
     * @param fornecedores Lista de fornecedores cadastrados.
     */
    public static void rankingFornecedoresPorMes(List<Estoque> estoques, List<Fornecedor> fornecedores) {
        // Mapa: Mês/Ano (MM/yyyy) -> (Fornecedor -> Quantidade total fornecida)
        Map<String, Map<String, Integer>> estoquePorMesFornecedor = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        // Itera sobre os registros de estoque
        for (Estoque estoque : estoques) {
            // Extrai mês e ano da data do estoque
            String mesAno = sdf.format(estoque.getDataEstoque());
            String idFornecedor = estoque.getIdFornecedor();
            int quantidade = estoque.getQtdEstoque();

            // Atualiza o total fornecido por fornecedor no mês
            estoquePorMesFornecedor
                    .computeIfAbsent(mesAno, k -> new HashMap<>())
                    .merge(idFornecedor, quantidade, Integer::sum);
        }

        // Mapa de ID do fornecedor -> Nome
        Map<String, String> fornecedorPorId = fornecedores.stream()
                .collect(Collectors.toMap(
                        Fornecedor::getIdFornecedor,
                        Fornecedor::getNomeFornecedor
                ));

        // Exibição dos dados ordenados cronologicamente
        System.out.println("\n|########## Ranking de Fornecedores por Mês ##########|");

        estoquePorMesFornecedor.keySet().stream()
                .sorted((mes1, mes2) -> {
                    try {
                        Date date1 = sdf.parse(mes1);
                        Date date2 = sdf.parse(mes2);
                        return date1.compareTo(date2);
                    } catch (Exception e) {
                        return mes1.compareTo(mes2); // fallback
                    }
                })
                .forEach(mesAno -> {
                    // Cabeçalho para cada mês
                    System.out.println("+--------------------------------+--------------------+");
                    System.out.printf("| %-51s |\n", "Mês - " + mesAno);
                    System.out.println("+--------------------------------+--------------------+");
                    System.out.printf("| %-31s| %-19s|\n",
                            centralizarTexto("Fornecedor", 31),
                            centralizarTexto("Estoque", 19));
                    System.out.println("+--------------------------------+--------------------+");

                    Map<String, Integer> fornecedorQtd = estoquePorMesFornecedor.get(mesAno);

                    // Ordena fornecedores pela quantidade fornecida (descendente)
                    fornecedorQtd.entrySet().stream()
                            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                            .forEach(entry -> {
                                String id = entry.getKey();
                                String nome = fornecedorPorId.getOrDefault(id, "Desconhecido");
                                int qtd = entry.getValue();
                                System.out.printf("|%-31s |%-20d|\n", nome, qtd);
                            });

                    System.out.println("+--------------------------------+--------------------+\n");
                });
    }
}
