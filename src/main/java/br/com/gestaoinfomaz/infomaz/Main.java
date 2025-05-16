package br.com.gestaoinfomaz.infomaz;
import br.com.gestaoinfomaz.listas.*;
import br.com.gestaoinfomaz.classes.*;
import java.util.List;

/**
 * Classe principal que orquestra a leitura dos dados da base Excel e executa
 * cálculos e rankings baseados nesses dados.
 *
 * Funciona como ponto de entrada da aplicação.
 */
public class Main {

    /**
     * Método principal que executa a aplicação Infomaz. Faz a leitura dos dados
     * do arquivo Excel e executa as métricas definidas.
     *
     * @param args argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        // Caminho do arquivo Excel com dados da base Infomaz
        String caminhoExcel = "Case_Infomaz_Base_de_Dados.xlsx";

        // Leitura das informações básicas dos arquivos Excel, distribuídas em diferentes listas:
        CadastroProdutos cadastroProdutos = new CadastroProdutos();
        List<Produto> produtos = cadastroProdutos.listaCadastroProdutos(caminhoExcel);

        CadastroClientes cadastroClientes = new CadastroClientes();
        List<Cliente> clientes = cadastroClientes.listaClientes(caminhoExcel);

        TransacoesVendas transacoesVendas = new TransacoesVendas(caminhoExcel);
        List<TransacaoVenda> transacoes = transacoesVendas.getTransacoes();

        CadastroEstoque cadastroEstoque = new CadastroEstoque();
        List<Estoque> estoques = cadastroEstoque.listaCadastroEstoque(caminhoExcel);

        CadastroFornecedores cadastroFornecedores = new CadastroFornecedores();
        List<Fornecedor> fornecedores = cadastroFornecedores.listaCadastroFornecedores(caminhoExcel);

        // Métricas Calculadas!
        // 1 - Total de vendas de produtos agrupados por categoria
        VendasCategoria.totalPorCategoria(produtos, transacoes);

        // 2 - Cálculo da margem de lucro por transação com base em vendas, produtos e estoque
        MargemLucros.margemPorTransacao(transacoes, produtos, estoques);

        // 3 - Ranking mensal de clientes por quantidade de produtos comprados
        RankingClientes.rankingClientesMes(clientes, transacoes);

        // 4 - Ranking mensal de fornecedores pela quantidade de estoque disponível
        RankingFornecedores.rankingFornecedoresPorMes(estoques, fornecedores);

        // 5 - Ranking mensal de produtos por quantidade vendida
        RankingVendaProdutos.rankingVendaPorMes(transacoes, produtos);

        // 6 - Ranking mensal de produtos por valor total vendido
        RankingValorProdutos.rankingValorPorMes(transacoes, produtos);

        // 7 - Média mensal do valor de venda por categoria de produto
        MediaCategoria.mediaPorCategoria(produtos, transacoes);

        // 8 - Ranking de margem de lucro por categoria de produto
        RankingMargemLucros.rankingMargemLucro(transacoes, produtos, estoques);

        // 9 - Listagem de produtos comprados por cada cliente
        ProdutosClientes.produtosPorCliente(clientes, produtos, transacoes);

        // 10 - Ranking de produtos pela quantidade disponível em estoque
        RankingProdutosEstoque.rankingProdutosQuantidade(produtos, estoques);
    }

    /**
     * Método auxiliar para centralizar texto em uma largura fixa.
     *
     * @param texto Texto que será centralizado
     * @param largura Número total de caracteres que o texto deve ocupar
     * @return String com o texto centralizado, preenchido com espaços
     */
    public static String centralizarTexto(String texto, int largura) {
        if (texto == null || texto.length() >= largura) {
            return texto;
        }

        StringBuilder sb = new StringBuilder();
        int espacos = largura - texto.length();
        int esquerda = espacos / 2;

        // Adiciona espaços à esquerda
        for (int i = 0; i < esquerda; i++) {
            sb.append(" ");
        }

        sb.append(texto);

        // Completa com espaços à direita até atingir a largura desejada
        while (sb.length() < largura) {
            sb.append(" ");
        }

        return sb.toString();
    }
}
