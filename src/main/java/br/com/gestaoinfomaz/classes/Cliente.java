package br.com.gestaoinfomaz.classes;
import java.util.Date;

/**
 * Classe que representa um cliente no sistema de gestão Infomaz. Armazena
 * informações como ID, nome e data de cadastro do cliente.
 */
public class Cliente {

    // Identificador único do cliente
    private int idCliente;

    // Nome completo do cliente
    private String nomeCliente;

    // Data em que o cliente foi cadastrado no sistema
    private Date dataCadastro;

    /**
     * Construtor da classe Cliente.
     *
     * @param idCliente Identificador do cliente
     * @param nomeCliente Nome do cliente
     * @param dataCadastro Data do cadastro do cliente
     */
    public Cliente(int idCliente, String nomeCliente, Date dataCadastro) {
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.dataCadastro = dataCadastro;
    }

    /**
     * Retorna o ID do cliente.
     *
     * @return idCliente
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * Retorna o nome do cliente.
     *
     * @return nomeCliente
     */
    public String getNomeCliente() {
        return nomeCliente;
    }

    /**
     * Retorna a data de cadastro do cliente.
     *
     * @return dataCadastro
     */
    public Date getDataCadastro() {
        return dataCadastro;
    }

    /**
     * Retorna uma representação em texto dos dados do cliente.
     *
     * @return String formatada com ID, nome e data de cadastro
     */
    @Override
    public String toString() {
        return String.format("ID Cliente: %d, Nome: %s, Data Cadastro: %s",
                idCliente, nomeCliente, dataCadastro);
    }
}
