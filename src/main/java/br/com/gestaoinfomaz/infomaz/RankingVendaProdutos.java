package br.com.gestaoinfomaz.infomaz;
import static br.com.gestaoinfomaz.infomaz.Main.centralizarTexto;
import br.com.gestaoinfomaz.classes.TransacaoVenda;
import br.com.gestaoinfomaz.classes.Produto;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe responsável por gerar um ranking mensal dos produtos
 * com base na quantidade total vendida.
 */
public class RankingVendaProdutos {

    /**
     * Exibe o ranking mensal dos produtos ordenado pela quantidade vendida.
     *
     * @param transacoesVendas Lista das transações de venda contendo quantidade e data da venda.
     * @param produtos Lista dos produtos cadastrados com seus respectivos IDs e nomes.
     */
    public static void rankingVendaPorMes(List<TransacaoVenda> transacoesVendas, List<Produto> produtos) {
        // Mapa que associa mês/ano (MM/yyyy) a outro mapa de ProdutoID -> Quantidade total vendida naquele mês
        Map<String, Map<Integer, Integer>> vendasPorMesProduto = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        // Percorre cada transação para agrupar a quantidade vendida por produto e mês
        for (TransacaoVenda venda : transacoesVendas) {
            String mesAno = sdf.format(venda.getDataNota());
            Integer idProduto = venda.getIdProduto();
            int quantidade = venda.getQtdItem();

            // Atualiza a quantidade total vendida do produto no mês correspondente
            vendasPorMesProduto
                .computeIfAbsent(mesAno, k -> new HashMap<>())
                .merge(idProduto, quantidade, Integer::sum);
        }

        // Mapa para associar o ID do produto ao seu nome, útil para exibição
        // Em caso de IDs duplicados, mantém o primeiro nome encontrado
        Map<Integer, String> produtoPorId = produtos.stream()
            .filter(p -> p.getIdProduto() != 0) // Ignora produtos com ID 0
            .collect(Collectors.toMap(
                Produto::getIdProduto,
                Produto::getNomeProduto,
                (nomeExistente, nomeNovo) -> nomeExistente
            ));

        // Impressão do ranking por mês, ordenado cronologicamente
        System.out.println("\n|## Ranking de Vendas de Produtos por Mês (Quantidade) ##|");
        vendasPorMesProduto.keySet().stream()
            .sorted((mes1, mes2) -> {
                try {
                    Date date1 = sdf.parse(mes1);
                    Date date2 = sdf.parse(mes2);
                    return date1.compareTo(date2);
                } catch (Exception e) {
                    return mes1.compareTo(mes2); // fallback para ordenação lexicográfica
                }
            })
            .forEach(mesAno -> {
                System.out.println("+-----------------------------------+--------------------+");
                System.out.printf("| %-54s |\n", "Mês - " + mesAno);
                System.out.println("+-----------------------------------+--------------------+");
                System.out.printf("| %-34s| %-19s|\n",
                        centralizarTexto("Produto", 34),
                        centralizarTexto("Quantidade Vendida", 19));
                System.out.println("+-----------------------------------+--------------------+");

                Map<Integer, Integer> produtoQtd = vendasPorMesProduto.get(mesAno);

                // Ordena os produtos daquele mês pela quantidade vendida, do maior para o menor
                produtoQtd.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                    .forEach(entry -> {
                        Integer id = entry.getKey();
                        String nome = produtoPorId.getOrDefault(id, "Desconhecido");
                        int qtd = entry.getValue();
                        System.out.printf("|%-34s |%-20d|\n", nome, qtd);
                    });
                System.out.println("+-----------------------------------+--------------------+\n");
            });
    }
}
