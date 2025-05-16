package br.com.gestaoinfomaz.infomaz;
import static br.com.gestaoinfomaz.infomaz.Main.centralizarTexto;
import br.com.gestaoinfomaz.classes.TransacaoVenda;
import br.com.gestaoinfomaz.classes.Produto;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe responsável por gerar um ranking mensal dos produtos com base no valor total vendido.
 */
public class RankingValorProdutos {

    /**
     * Exibe o ranking mensal dos produtos ordenado pelo valor total vendido.
     *
     * @param transacoesVendas Lista das transações de venda contendo quantidade, valor e data da venda.
     * @param produtos Lista dos produtos cadastrados com seus respectivos IDs e nomes.
     */
    public static void rankingValorPorMes(List<TransacaoVenda> transacoesVendas, List<Produto> produtos) {
        // Mapa que associa mês/ano (MM/yyyy) a outro mapa de ProdutoID -> Valor total vendido naquele mês
        Map<String, Map<Integer, Double>> vendasValorPorMesProduto = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        // Percorre cada transação para agrupar o valor total vendido por produto e mês
        for (TransacaoVenda venda : transacoesVendas) {
            if (venda.getDataNota() == null) continue; // Ignora transações sem data

            String mesAno = sdf.format(venda.getDataNota()); // Formata data para MM/yyyy
            Integer idProduto = venda.getIdProduto();
            double valorTotalItem = venda.getQtdItem() * venda.getValorItem();

            // Atualiza o valor total vendido do produto no mês correspondente
            vendasValorPorMesProduto
                .computeIfAbsent(mesAno, k -> new HashMap<>())
                .merge(idProduto, valorTotalItem, Double::sum);
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
        System.out.println("\n|#### Ranking de Vendas de Produtos por Mês (Valor) #####|");
        vendasValorPorMesProduto.keySet().stream()
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
                        centralizarTexto("Valor Total", 19));
                System.out.println("+-----------------------------------+--------------------+");

                Map<Integer, Double> produtoValor = vendasValorPorMesProduto.get(mesAno);

                // Ordena os produtos daquele mês pelo valor total vendido, do maior para o menor
                produtoValor.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                    .forEach(entry -> {
                        Integer id = entry.getKey();
                        String nome = produtoPorId.getOrDefault(id, "Desconhecido");
                        double valor = entry.getValue();
                        System.out.printf("|%-34s | %19.2f|\n", nome, valor);
                    });
                System.out.println("+-----------------------------------+--------------------+\n");
            });
    }
}
