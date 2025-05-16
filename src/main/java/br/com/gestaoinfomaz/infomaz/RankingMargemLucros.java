package br.com.gestaoinfomaz.infomaz;
import static br.com.gestaoinfomaz.infomaz.Main.centralizarTexto;
import br.com.gestaoinfomaz.classes.Produto;
import br.com.gestaoinfomaz.classes.TransacaoVenda;
import br.com.gestaoinfomaz.classes.Estoque;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe responsável por gerar um ranking de margem de lucros por categoria de produto.
 * O cálculo considera as transações de vendas, produtos cadastrados e o estoque disponível.
 */
public class RankingMargemLucros {

    /**
     * Método que calcula e exibe o ranking de margem de lucro médio e lucro percentual médio
     * para cada categoria de produto.
     *
     * @param transacoes Lista das transações de venda, contendo id do produto e valor da venda.
     * @param produtos Lista dos produtos cadastrados, com suas categorias e IDs relacionados.
     * @param estoques Lista dos estoques, incluindo quantidade e valor total em estoque.
     */
    public static void rankingMargemLucro(List<TransacaoVenda> transacoes,
            List<Produto> produtos,
            List<Estoque> estoques) {
        
        // Obtém todas as categorias únicas não nulas e não vazias presentes nos produtos
        Set<String> todasCategorias = produtos.stream()
                .map(Produto::getCategoria)
                .filter(c -> c != null && !c.isEmpty())
                .collect(Collectors.toSet());

        // Cria um mapa para acessar produtos pelo seu ID, ignorando IDs zerados
        Map<Integer, Produto> mapaProdutos = produtos.stream()
                .filter(p -> p.getIdProduto() != 0)
                .collect(Collectors.toMap(
                        Produto::getIdProduto,
                        p -> p,
                        (existente, novo) -> existente)); // Em caso de duplicidade, mantém o existente

        // Cria um mapa para acessar estoques pelo seu ID, ignorando IDs zerados
        Map<Integer, Estoque> mapaEstoques = estoques.stream()
                .filter(e -> e.getIdEstoque() != 0)
                .collect(Collectors.toMap(
                        Estoque::getIdEstoque,
                        e -> e,
                        (existente, novo) -> existente)); // Em caso de duplicidade, mantém o existente

        // Mapas para armazenar margens e custos unitários por categoria, para cálculo posterior
        Map<String, List<Double>> margensPorCategoria = new HashMap<>();
        Map<String, List<Double>> custosPorCategoria = new HashMap<>();

        // Inicializa as listas vazias para cada categoria no mapa
        todasCategorias.forEach(categoria -> {
            margensPorCategoria.put(categoria, new ArrayList<>());
            custosPorCategoria.put(categoria, new ArrayList<>());
        });

        // Processa cada transação de venda para calcular margem e custo unitário
        for (TransacaoVenda venda : transacoes) {
            Produto produto = mapaProdutos.get(venda.getIdProduto());
            if (produto == null) {
                // Produto não encontrado, ignora a transação
                continue;
            }

            Estoque estoque = mapaEstoques.get(produto.getIdEstoque());
            if (estoque == null || estoque.getQtdEstoque() == 0) {
                // Estoque não encontrado ou quantidade zero, ignora esta transação
                continue;
            }

            // Calcula o custo unitário como valor total do estoque dividido pela quantidade
            double custoUnitario = estoque.getValorEstoque() / estoque.getQtdEstoque();
            // Calcula margem como valor da venda menos custo unitário
            double margem = venda.getValorItem() - custoUnitario;

            // Adiciona margem na lista da categoria do produto
            margensPorCategoria
                    .computeIfAbsent(produto.getCategoria(), k -> new ArrayList<>())
                    .add(margem);

            // Adiciona custo unitário na lista da categoria do produto
            custosPorCategoria
                    .computeIfAbsent(produto.getCategoria(), k -> new ArrayList<>())
                    .add(custoUnitario);
        }

        // Mapas para armazenar a média da margem e a média do lucro percentual por categoria
        Map<String, Double> mediaMargemPorCategoria = new HashMap<>();
        Map<String, Double> mediaLucroPercentualPorCategoria = new HashMap<>();

        // Calcula as médias para cada categoria
        todasCategorias.forEach(categoria -> {
            List<Double> margens = margensPorCategoria.getOrDefault(categoria, new ArrayList<>());
            List<Double> custos = custosPorCategoria.getOrDefault(categoria, new ArrayList<>());

            // Média aritmética das margens da categoria
            double mediaMargem = margens.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            // Soma dos custos para cálculo do percentual
            double somaCustos = custos.stream().mapToDouble(Double::doubleValue).sum();

            // Calcula lucro percentual: soma das margens sobre soma dos custos * 100, evita divisão por zero
            double mediaLucroPercentual = somaCustos > 0
                    ? (margens.stream().mapToDouble(Double::doubleValue).sum() / somaCustos) * 100
                    : 0;

            mediaMargemPorCategoria.put(categoria, mediaMargem);
            mediaLucroPercentualPorCategoria.put(categoria, mediaLucroPercentual);
        });

        // Exibe o ranking ordenado pela margem média decrescente
        System.out.println("\n|####### Ranking de Margem de Lucro por Categoria ########|");
        System.out.println("+----------------------+-------------------+--------------+");
        System.out.printf("| %-20s | %-12s | %-12s |\n",
                centralizarTexto("Categoria", 20),
                centralizarTexto("Margem Média (R$)", 12),
                centralizarTexto("Lucro (%)", 12));
        System.out.println("+----------------------+-------------------+--------------+");

        mediaMargemPorCategoria.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> {
                    String categoria = entry.getKey();
                    double mediaMargem = entry.getValue();
                    double lucroPercentual = mediaLucroPercentualPorCategoria.getOrDefault(categoria, 0.0);
                    System.out.printf("|%-21s | %18.2f| %13.1f|\n",
                            categoria, mediaMargem, lucroPercentual);
                });

        System.out.println("+----------------------+-------------------+--------------+");
    }
}
