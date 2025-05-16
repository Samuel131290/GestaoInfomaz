package br.com.gestaoinfomaz.infomaz;
import static br.com.gestaoinfomaz.infomaz.Main.centralizarTexto;
import br.com.gestaoinfomaz.classes.Estoque;
import br.com.gestaoinfomaz.classes.Produto;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe responsável por gerar um ranking dos produtos baseado na quantidade disponível em estoque.
 */
public class RankingProdutosEstoque {

    /**
     * Exibe o ranking de produtos ordenado pela quantidade disponível em estoque, do maior para o menor.
     *
     * @param produtos Lista dos produtos cadastrados, contendo nome e referência ao estoque.
     * @param estoques Lista dos estoques com informações de quantidade disponíveis.
     */
    public static void rankingProdutosQuantidade(List<Produto> produtos, List<Estoque> estoques) {
        // Cria um mapa para acesso rápido ao estoque pelo ID do estoque
        Map<Integer, Estoque> estoquePorId = estoques.stream()
                .collect(Collectors.toMap(Estoque::getIdEstoque, e -> e));

        // Lista para armazenar pares Produto-Nome e quantidade em estoque correspondentes
        List<Map.Entry<String, Integer>> produtosComEstoque = new ArrayList<>();

        // Itera sobre cada produto para associar sua quantidade em estoque
        for (Produto produto : produtos) {
            Estoque estoque = estoquePorId.get(produto.getIdEstoque());
            if (estoque != null) {
                // Adiciona o nome do produto e sua quantidade disponível na lista
                produtosComEstoque.add(new AbstractMap.SimpleEntry<>(
                        produto.getNomeProduto(),
                        estoque.getQtdEstoque()
                ));
            }
        }

        // Ordena a lista de produtos pela quantidade em estoque em ordem decrescente
        produtosComEstoque.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Exibição do ranking formatado no console
        System.out.println("\n|##### Ranking de Produtos por Quantidade em Estoque ####|");
        System.out.println("+-----------------------------------+--------------------+");
        System.out.printf("| %-34s| %-19s|\n",
                centralizarTexto("Produto", 34),
                centralizarTexto("Quantidade", 19));
        System.out.println("+-----------------------------------+--------------------+");

        // Imprime cada produto com sua quantidade em estoque
        for (Map.Entry<String, Integer> entry : produtosComEstoque) {
            System.out.printf("|%-34s |%-20d|\n", entry.getKey(), entry.getValue());
        }

        System.out.println("+-----------------------------------+--------------------+\n");
    }
}
