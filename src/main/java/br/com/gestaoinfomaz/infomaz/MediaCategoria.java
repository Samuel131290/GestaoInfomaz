package br.com.gestaoinfomaz.infomaz;
import static br.com.gestaoinfomaz.infomaz.Main.centralizarTexto;
import br.com.gestaoinfomaz.classes.Produto;
import br.com.gestaoinfomaz.classes.TransacaoVenda;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe responsável por calcular e exibir a média de valor vendido por
 * categoria de produto, agrupando os dados por mês/ano a partir das transações
 * de vendas.
 */
public class MediaCategoria {

    /**
     * Calcula e exibe a média de valor de venda por categoria, agrupado por
     * mês/ano. A média é obtida dividindo o total de valor vendido pelo total
     * de itens vendidos para cada categoria em cada mês.
     *
     * @param produtos Lista de produtos cadastrados
     * @param transacoesVendas Lista de transações de vendas
     */
    public static void mediaPorCategoria(List<Produto> produtos, List<TransacaoVenda> transacoesVendas) {
        // Cria um mapa de ID do produto -> categoria, ignorando produtos com ID 0.
        // Em caso de duplicatas, mantém a primeira ocorrência.
        Map<Integer, String> categoriaPorIdProduto = produtos.stream()
                .filter(p -> p.getIdProduto() != 0)
                .collect(Collectors.toMap(
                        Produto::getIdProduto,
                        Produto::getCategoria,
                        (categoriaExistente, categoriaNova) -> categoriaExistente));

        // Mapa que agrupa os dados por mês/ano -> categoria -> [totalVenda, quantidadeVendida]
        Map<String, Map<String, double[]>> dadosPorMesCategoria = new HashMap<>();

        // Formatador para extrair MM/yyyy da data da nota
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        // Processa cada transação de venda
        for (TransacaoVenda venda : transacoesVendas) {
            String mesAno = sdf.format(venda.getDataNota());
            int idProduto = venda.getIdProduto();
            int qtdItem = venda.getQtdItem();
            double valorItem = venda.getValorItem();
            double totalVenda = valorItem * qtdItem;

            // Obtém a categoria do produto, ou "Desconhecida" caso não encontrada
            String categoria = categoriaPorIdProduto.getOrDefault(idProduto, "Desconhecida");

            // Inicializa estruturas se necessário
            dadosPorMesCategoria
                    .computeIfAbsent(mesAno, k -> new HashMap<>())
                    .computeIfAbsent(categoria, k -> new double[2]);

            // Atualiza total vendido e quantidade para a categoria no mês
            double[] valores = dadosPorMesCategoria.get(mesAno).get(categoria);
            valores[0] += totalVenda;
            valores[1] += qtdItem;
        }

        // Exibe o resultado por mês, ordenado cronologicamente
        System.out.println("\n|######## Média de Valor de Venda por Categoria #########|");

        dadosPorMesCategoria.keySet().stream()
                .sorted((m1, m2) -> {
                    try {
                        return sdf.parse(m1).compareTo(sdf.parse(m2));
                    } catch (Exception e) {
                        return m1.compareTo(m2);
                    }
                })
                .forEach(mesAno -> {
                    System.out.println("+-----------------------------------+--------------------+");
                    System.out.printf("| %-54s |\n", "Mês - " + mesAno);
                    System.out.println("+-----------------------------------+--------------------+");
                    System.out.printf("| %-34s| %-19s|\n",
                            centralizarTexto("Categoria", 34),
                            centralizarTexto("Média Valor Vendido", 19));
                    System.out.println("+-----------------------------------+--------------------+");

                    Map<String, double[]> categorias = dadosPorMesCategoria.get(mesAno);

                    categorias.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey()) // Ordena as categorias por nome
                            .forEach(entry -> {
                                String categoria = entry.getKey();
                                double[] valores = entry.getValue();
                                double total = valores[0];
                                double quantidade = valores[1];
                                double media = quantidade > 0 ? total / quantidade : 0;
                                System.out.printf("|%-34s | %19.2f|\n", categoria, media);
                            });
                    System.out.println("+-----------------------------------+--------------------+\n");
                });
    }
}
