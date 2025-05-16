package br.com.gestaoinfomaz.infomaz;
import static br.com.gestaoinfomaz.infomaz.Main.centralizarTexto;
import br.com.gestaoinfomaz.classes.Produto;
import br.com.gestaoinfomaz.classes.TransacaoVenda;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável por calcular e exibir o total de vendas
 * agrupado por categoria de produtos.
 */
public class VendasCategoria {

    /**
     * Calcula o total de vendas por categoria com base nas transações de venda
     * e exibe o resultado formatado no console.
     *
     * @param produtos Lista contendo os produtos com seus respectivos IDs e categorias.
     * @param transacoes Lista contendo as transações de venda com produto, quantidade e valor.
     */
    public static void totalPorCategoria(List<Produto> produtos, List<TransacaoVenda> transacoes) {
        System.out.println("\n|### Total de Vendas por Categoria ###|");
        System.out.println("+------------------+------------------+");
        System.out.printf("| %-15s | %-15s |\n",
                centralizarTexto("Categoria ", 16),
                centralizarTexto("Total de Vendas", 16));
        System.out.println("+------------------+------------------+");

        // Mapeia o ID do produto para sua categoria para facilitar buscas rápidas
        Map<Integer, String> produtoParaCategoria = new HashMap<>();
        for (Produto p : produtos) {
            produtoParaCategoria.put(p.getIdProduto(), p.getCategoria());
        }

        // Mapa que acumula o total de vendas por categoria
        Map<String, Double> totalPorCategoria = new HashMap<>();

        // Para cada transação, obtém a categoria do produto e soma o valor total da venda
        for (TransacaoVenda t : transacoes) {
            String categoria = produtoParaCategoria.get(t.getIdProduto());
            if (categoria != null) {
                double valorTotalItem = t.getValorItem() * t.getQtdItem();
                totalPorCategoria.put(categoria,
                    totalPorCategoria.getOrDefault(categoria, 0.0) + valorTotalItem);
            }
        }

        // Exibe o total de vendas por categoria formatado em tabela
        for (Map.Entry<String, Double> entry : totalPorCategoria.entrySet()) {
            System.out.printf("|%-17s | %17.2f|%n", entry.getKey(), entry.getValue());
        }
        System.out.println("+-------------------------------------+");
    }
}
