package br.com.gestaoinfomaz.classes;
import java.util.Date;

/**
 * Classe que representa uma transação de venda no sistema de gestão Infomaz.
 * Contém informações relacionadas à nota fiscal, valores da venda, produto vendido e cliente associado.
 */
public class TransacaoVenda {

    // Identificador da nota fiscal da venda
    private int idNota;

    // Data da emissão da nota fiscal
    private Date dataNota;

    // Valor total da nota fiscal
    private double valorNota;

    // Valor unitário do item vendido
    private double valorItem;

    // Quantidade de itens vendidos
    private int qtdItem;

    // Identificador do produto vendido
    private int idProduto;

    // Identificador do cliente que realizou a compra
    private int idCliente;

    /**
     * Construtor da classe TransacaoVenda.
     *
     * @param idNota      Identificador da nota fiscal
     * @param dataNota    Data da nota fiscal
     * @param valorNota   Valor total da nota fiscal
     * @param valorItem   Valor unitário do item vendido
     * @param qtdItem     Quantidade de itens vendidos
     * @param idProduto   Identificador do produto
     * @param idCliente   Identificador do cliente
     */
    public TransacaoVenda(int idNota, Date dataNota, double valorNota, double valorItem,
                          int qtdItem, int idProduto, int idCliente) {
        this.idNota = idNota;
        this.dataNota = dataNota;
        this.valorNota = valorNota;
        this.valorItem = valorItem;
        this.qtdItem = qtdItem;
        this.idProduto = idProduto;
        this.idCliente = idCliente;
    }

    // Métodos de acesso (getters)

    /**
     * Retorna o ID da nota fiscal.
     * @return idNota
     */
    public int getIdNota() { return idNota; }

    /**
     * Retorna a data da nota fiscal.
     * @return dataNota
     */
    public Date getDataNota() { return dataNota; }

    /**
     * Retorna o valor total da nota fiscal.
     * @return valorNota
     */
    public double getValorNota() { return valorNota; }

    /**
     * Retorna o valor unitário do item vendido.
     * @return valorItem
     */
    public double getValorItem() { return valorItem; }

    /**
     * Retorna a quantidade de itens vendidos.
     * @return qtdItem
     */
    public int getQtdItem() { return qtdItem; }

    /**
     * Retorna o identificador do produto vendido.
     * @return idProduto
     */
    public int getIdProduto() { return idProduto; }

    /**
     * Retorna o identificador do cliente que realizou a compra.
     * @return idCliente
     */
    public int getIdCliente() { return idCliente; }

    /**
     * Retorna uma representação textual da transação de venda.
     * @return String com os dados da transação
     */
    @Override
    public String toString() {
        return "TransacaoVenda{" +
                "idNota=" + idNota +
                ", dataNota=" + dataNota +
                ", valorNota=" + valorNota +
                ", valorItem=" + valorItem +
                ", qtdItem=" + qtdItem +
                ", idProduto=" + idProduto +
                ", idCliente=" + idCliente +
                '}';
    }
}
