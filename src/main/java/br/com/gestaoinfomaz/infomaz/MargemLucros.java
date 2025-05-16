package br.com.gestaoinfomaz.infomaz;
import static br.com.gestaoinfomaz.infomaz.Main.centralizarTexto;
import br.com.gestaoinfomaz.classes.Estoque;
import br.com.gestaoinfomaz.classes.Produto;
import br.com.gestaoinfomaz.classes.TransacaoVenda;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe responsável pelo cálculo e exibição da margem de lucro por produto,
 * utilizando os dados das transações de vendas, produtos e estoques.
 */
public class MargemLucros {

    /**
     * Calcula e imprime a margem de lucro por produto com base nas transações de vendas.
     * A margem é calculada pela diferença entre o valor médio de venda (por item)
     * e o custo unitário (valor do estoque dividido pela quantidade disponível).
     *
     * @param transacoes Lista de transações de vendas
     * @param produtos Lista de produtos cadastrados
     * @param estoques Lista de registros de estoque
     */
    public static void margemPorTransacao(List<TransacaoVenda> transacoes, List<Produto> produtos, List<Estoque> estoques) {

        // Cabeçalho da tabela de saída formatada
        System.out.println("\n\n|####################### Margem de Lucro por Produto #######################|");
        System.out.println("+--------------------------------+----------------+------------+------------+");
        System.out.printf("| %-30s | %-14s | %-10s | %-10s |\n",
                centralizarTexto("Produto", 30),
                centralizarTexto("Custo Unitário", 14),
                centralizarTexto("Venda", 10),
                centralizarTexto("Margem", 10));
        System.out.println("+--------------------------------+----------------+------------+------------+");

        // Cria um mapa de produtos válidos (ID > 0), evitando duplicatas
        Map<Integer, Produto> mapaProdutos = produtos.stream()
                .filter(p -> p.getIdProduto() > 0)
                .collect(Collectors.toMap(
                        Produto::getIdProduto,
                        p -> p,
                        (existente, novo) -> existente));

        // Cria um mapa de estoques válidos (ID > 0), evitando duplicatas
        Map<Integer, Estoque> mapaEstoques = estoques.stream()
                .filter(e -> e.getIdEstoque() > 0)
                .collect(Collectors.toMap(
                        Estoque::getIdEstoque,
                        e -> e,
                        (existente, novo) -> existente));

        // Agrupa as transações por ID de produto
        Map<Integer, List<TransacaoVenda>> transacoesPorProduto = transacoes.stream()
                .filter(t -> t.getIdProduto() > 0)
                .collect(Collectors.groupingBy(TransacaoVenda::getIdProduto));

        // Processa cada produto com transações registradas
        for (Map.Entry<Integer, List<TransacaoVenda>> entry : transacoesPorProduto.entrySet()) {
            int idProduto = entry.getKey();
            Produto produto = mapaProdutos.get(idProduto);

            // Verifica se o produto existe no cadastro
            if (produto == null) {
                System.out.printf("| Produto ID %d não encontrado %29s |\n", idProduto, "");
                continue;
            }

            // Obtém o estoque vinculado ao produto
            Estoque estoque = Optional.ofNullable(produto.getIdEstoque())
                    .map(mapaEstoques::get)
                    .orElse(null);

            // Valida o estoque (deve existir e ter quantidade > 0)
            if (estoque == null || estoque.getQtdEstoque() == 0) {
                System.out.printf("| Estoque inválido para %-25s %18s |\n",
                        produto.getNomeProduto(), "");
                continue;
            }

            // Cálculo do custo unitário: valor total do estoque dividido pela quantidade
            double custoUnitario = estoque.getValorEstoque() / estoque.getQtdEstoque();

            // Calcula a média do valor de venda por item para o produto
            double valorVendaMedio = entry.getValue().stream()
                    .mapToDouble(TransacaoVenda::getValorItem)
                    .average()
                    .orElse(0);

            // Calcula a margem de lucro: valor médio de venda - custo unitário
            double margem = valorVendaMedio - custoUnitario;

            // Exibe os dados formatados na tabela
            System.out.printf("|%-31s | %15.2f| %11.2f| %11.2f|\n", produto.getNomeProduto(), custoUnitario, valorVendaMedio, margem);
        }

        // Rodapé da tabela
        System.out.println("+---------------------------------------------------------------------------+");
    }
}
