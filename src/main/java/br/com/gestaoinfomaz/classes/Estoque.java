package br.com.gestaoinfomaz.classes;
import java.util.Date;

/**
 * Classe que representa um item de estoque no sistema de gestão Infomaz.
 * Contém informações sobre o valor, quantidade, data de entrada e fornecedor.
 */
public class Estoque {

    // Identificador único do registro de estoque
    private int idEstoque;

    // Valor unitário do item em estoque
    private double valorEstoque;

    // Quantidade disponível em estoque
    private int qtdEstoque;

    // Data de entrada ou atualização do item no estoque
    private Date dataEstoque;

    // Identificador do fornecedor associado ao item
    private String idFornecedor;

    /**
     * Construtor da classe Estoque.
     *
     * @param idEstoque     Identificador do estoque
     * @param valorEstoque  Valor unitário do item
     * @param qtdEstoque    Quantidade disponível no estoque
     * @param dataEstoque   Data da entrada ou atualização do estoque
     * @param idFornecedor  Identificador do fornecedor do item
     */
    public Estoque(int idEstoque, double valorEstoque, int qtdEstoque, Date dataEstoque, String idFornecedor) {
        this.idEstoque = idEstoque;
        this.valorEstoque = valorEstoque;
        this.qtdEstoque = qtdEstoque;
        this.dataEstoque = dataEstoque;
        this.idFornecedor = idFornecedor;
    }

    /**
     * Retorna o ID do estoque.
     *
     * @return idEstoque
     */
    public int getIdEstoque() {
        return idEstoque;
    }

    /**
     * Retorna o valor unitário do item em estoque.
     *
     * @return valorEstoque
     */
    public double getValorEstoque() {
        return valorEstoque;
    }

    /**
     * Retorna a quantidade disponível no estoque.
     *
     * @return qtdEstoque
     */
    public int getQtdEstoque() {
        return qtdEstoque;
    }

    /**
     * Retorna a data de entrada ou atualização do estoque.
     *
     * @return dataEstoque
     */
    public Date getDataEstoque() {
        return dataEstoque;
    }

    /**
     * Retorna o identificador do fornecedor do item.
     *
     * @return idFornecedor
     */
    public String getIdFornecedor() {
        return idFornecedor;
    }

    /**
     * Retorna uma representação textual dos dados do estoque.
     *
     * @return String formatada com ID, valor, quantidade, data e fornecedor
     */
    @Override
    public String toString() {
        return String.format("ID Estoque: %d, Valor: R$ %.2f, Quantidade: %d, Data: %s, Fornecedor: %s",
                idEstoque, valorEstoque, qtdEstoque, dataEstoque, idFornecedor);
    }
}
