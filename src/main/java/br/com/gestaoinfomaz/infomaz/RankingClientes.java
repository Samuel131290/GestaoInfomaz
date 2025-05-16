package br.com.gestaoinfomaz.infomaz;
import static br.com.gestaoinfomaz.infomaz.Main.centralizarTexto;
import br.com.gestaoinfomaz.classes.Cliente;
import br.com.gestaoinfomaz.classes.TransacaoVenda;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.*;

/**
 * Classe responsável por gerar um ranking mensal de clientes com base
 * na quantidade total de produtos comprados em cada mês.
 */
public class RankingClientes {

    /**
     * Gera e exibe um ranking mensal de clientes com base na quantidade de produtos comprados.
     * Para cada mês, lista os clientes ordenados do que mais comprou para o que menos comprou.
     *
     * @param clientes Lista de clientes cadastrados.
     * @param transacoes Lista de transações de vendas realizadas.
     */
    public static void rankingClientesMes(List<Cliente> clientes, List<TransacaoVenda> transacoes) {
        // Mapa: Mês/Ano (MM/yyyy) -> Mapa de ID do Cliente -> Quantidade total de itens comprados
        Map<String, Map<Integer, Integer>> comprasPorMes = new HashMap<>();
        DateTimeFormatter formatoMesAno = DateTimeFormatter.ofPattern("MM/yyyy");

        // Agrupa compras por mês/ano e por cliente
        for (TransacaoVenda venda : transacoes) {
            // Converte a data da venda para LocalDate e extrai MM/yyyy
            LocalDate data = venda.getDataNota().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            String mesAno = data.format(formatoMesAno);

            // Inicializa estruturas, se necessário
            comprasPorMes.putIfAbsent(mesAno, new HashMap<>());
            Map<Integer, Integer> comprasCliente = comprasPorMes.get(mesAno);

            // Soma a quantidade de itens comprados por cliente
            int idCliente = venda.getIdCliente();
            comprasCliente.put(idCliente, comprasCliente.getOrDefault(idCliente, 0) + venda.getQtdItem());
        }

        // Ordena os meses cronologicamente
        List<String> mesesOrdenados = new ArrayList<>(comprasPorMes.keySet());
        mesesOrdenados.sort((m1, m2) -> {
            LocalDate d1 = LocalDate.parse("01/" + m1, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate d2 = LocalDate.parse("01/" + m2, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return d1.compareTo(d2);
        });

        // Exibe o ranking de clientes por mês
        System.out.println("\n\n|############ Ranking de Clientes por Mês ############|");

        for (String mesAno : mesesOrdenados) {
            System.out.println("+--------------------------------+--------------------+");
            System.out.printf("| %-51s |\n", "Mês - " + mesAno);
            System.out.println("+--------------------------------+--------------------+");
            System.out.printf("| %-31s| %-5s |\n",
                    centralizarTexto("Cliente", 31),
                    centralizarTexto("Produtos Comprados", 5));
            System.out.println("+--------------------------------+--------------------+");

            Map<Integer, Integer> compras = comprasPorMes.get(mesAno);

            // Ordena os clientes pela quantidade comprada (decrescente)
            compras.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                    .forEach(entry -> {
                        int idCliente = entry.getKey();
                        int totalProdutos = entry.getValue();

                        // Busca o nome do cliente pelo ID
                        String nomeCliente = clientes.stream()
                                .filter(c -> c.getIdCliente() == idCliente)
                                .map(Cliente::getNomeCliente)
                                .findFirst()
                                .orElse("Cliente Desconhecido");

                        // Exibe os dados formatados
                        System.out.printf("|%-31s |%-20d|\n", nomeCliente, totalProdutos);
                    });

            System.out.println("+--------------------------------+--------------------+\n");
        }
    }
}
