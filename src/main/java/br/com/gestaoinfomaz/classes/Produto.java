package br.com.gestaoinfomaz.classes;

/**
 * Classe que representa um produto no sistema de gestão Infomaz.
 * Contém informações como o ID do produto, ID do estoque relacionado,
 * nome do produto e sua categoria.
 */
public class Produto {

    // Identificador único do produto
    private int idProduto;

    // Identificador do estoque associado ao produto
    private int idEstoque;

    // Nome do produto
    private String nomeProduto;

    // Categoria à qual o produto pertence (ex: Eletrônicos, Vestuário, etc.)
    private String categoria;

    /**
     * Construtor da classe Produto.
     *
     * @param idProduto    Identificador do produto
     * @param idEstoque    Identificador do estoque associado
     * @param nomeProduto  Nome do produto
     * @param categoria    Categoria do produto
     */
    public Produto(int idProduto, int idEstoque, String nomeProduto, String categoria) {
        this.idProduto = idProduto;
        this.idEstoque = idEstoque;
        this.nomeProduto = nomeProduto;
        this.categoria = categoria;
    }

    /**
     * Retorna o ID do produto.
     *
     * @return idProduto
     */
    public int getIdProduto() {
        return idProduto;
    }

    /**
     * Retorna o ID do estoque associado ao produto.
     *
     * @return idEstoque
     */
    public int getIdEstoque() {
        return idEstoque;
    }

    /**
     * Retorna o nome do produto.
     *
     * @return nomeProduto
     */
    public String getNomeProduto() {
        return nomeProduto;
    }

    /**
     * Retorna a categoria do produto.
     *
     * @return categoria
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Retorna uma representação textual dos dados do produto.
     *
     * @return String formatada com ID, estoque, nome e categoria
     */
    @Override
    public String toString() {
        return String.format("ID Produto: %d, ID Estoque: %d, Nome: %s, Categoria: %s",
                idProduto, idEstoque, nomeProduto, categoria);
    }
}
