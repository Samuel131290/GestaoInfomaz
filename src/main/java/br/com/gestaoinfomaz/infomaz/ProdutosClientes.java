package br.com.gestaoinfomaz.infomaz;
import static br.com.gestaoinfomaz.infomaz.Main.centralizarTexto;
import br.com.gestaoinfomaz.classes.Cliente;
import br.com.gestaoinfomaz.classes.Produto;
import br.com.gestaoinfomaz.classes.TransacaoVenda;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe responsável por gerar relatórios que relacionam produtos comprados por clientes,
 * utilizando listas de clientes, produtos e transações de venda.
 */
public class ProdutosClientes {

    /**
     * Método que recebe listas de clientes, produtos e transações de venda e exibe no console
     * uma lista formatada mostrando os produtos adquiridos por cada cliente, bem como as quantidades.
     *
     * @param clientes   Lista de objetos Cliente contendo informações dos clientes
     * @param produtos   Lista de objetos Produto contendo informações dos produtos
     * @param transacoes Lista de objetos TransacaoVenda contendo registros de vendas realizadas
     */
    public static void produtosPorCliente(List<Cliente> clientes,
            List<Produto> produtos,
            List<TransacaoVenda> transacoes) {

        // Cria um mapa para associar ID do cliente ao nome, tratando possíveis IDs duplicados.
        // Caso haja duplicatas, prioriza o nome não vazio.
        Map<Integer, String> clientePorId = clientes.stream()
                .collect(Collectors.toMap(
                        Cliente::getIdCliente,
                        Cliente::getNomeCliente,
                        (nome1, nome2) -> !nome1.isBlank() ? nome1 : nome2
                ));

        // Cria um mapa para associar ID do produto ao nome, também tratando duplicatas da mesma forma.
        Map<Integer, String> produtoPorId = produtos.stream()
                .collect(Collectors.toMap(
                        Produto::getIdProduto,
                        Produto::getNomeProduto,
                        (nome1, nome2) -> !nome1.isBlank() ? nome1 : nome2
                ));

        // Mapa que relaciona o ID do cliente a um mapa dos produtos comprados,
        // onde a chave é o ID do produto e o valor é a quantidade total comprada.
        Map<Integer, Map<Integer, Integer>> produtosPorCliente = new HashMap<>();

        // Percorre todas as transações de venda para preencher o mapa produtosPorCliente
        for (TransacaoVenda venda : transacoes) {
            int idCliente = venda.getIdCliente();
            int idProduto = venda.getIdProduto();
            int quantidade = venda.getQtdItem();

            // Para cada venda, atualiza o total de quantidade do produto comprado pelo cliente
            produtosPorCliente
                    .computeIfAbsent(idCliente, k -> new HashMap<>())
                    .merge(idProduto, quantidade, Integer::sum);
        }

        // Imprime o cabeçalho do relatório no console
        System.out.println("\n\n|####### Lista de Produtos Comprados por Cliente ########|");

        // Ordena os clientes por nome para exibição organizada
        List<Map.Entry<Integer, String>> clientesOrdenados = clientePorId.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        // Para cada cliente ordenado, imprime os produtos que ele comprou e as quantidades
        for (Map.Entry<Integer, String> entryCliente : clientesOrdenados) {
            int idCliente = entryCliente.getKey();
            String nomeCliente = entryCliente.getValue();

            // Obtém os produtos comprados pelo cliente
            Map<Integer, Integer> produtosComprados = produtosPorCliente.get(idCliente);
            if (produtosComprados == null || produtosComprados.isEmpty()) {
                continue; // Pula clientes que não realizaram compras
            }

            // Imprime cabeçalhos formatados para a tabela de produtos do cliente
            System.out.println("+-----------------------------------+--------------------+");
            System.out.printf("| %-54s |\n", "Cliente - " + nomeCliente);
            System.out.println("+-----------------------------------+--------------------+");
            System.out.printf("| %-34s| %-19s|\n",
                    centralizarTexto("Produtos Comprados", 34),
                    centralizarTexto("Quantidade", 19));
            System.out.println("+-----------------------------------+--------------------+");

            // Ordena os produtos comprados por nome para apresentação ordenada
            List<Map.Entry<Integer, Integer>> produtosOrdenados = produtosComprados.entrySet().stream()
                    .sorted((e1, e2) -> {
                        String nome1 = produtoPorId.getOrDefault(e1.getKey(), "");
                        String nome2 = produtoPorId.getOrDefault(e2.getKey(), "");
                        return nome1.compareTo(nome2);
                    })
                    .collect(Collectors.toList());

            // Imprime linha por linha os produtos e quantidades adquiridas pelo cliente
            for (Map.Entry<Integer, Integer> entryProduto : produtosOrdenados) {
                int idProduto = entryProduto.getKey();
                String nomeProduto = produtoPorId.getOrDefault(idProduto, "Desconhecido");
                int quantidade = entryProduto.getValue();

                System.out.printf("|%-34s |%-20d|\n", nomeProduto, quantidade);
            }
            System.out.println("+-----------------------------------+--------------------+\n");
        }
    }
}
