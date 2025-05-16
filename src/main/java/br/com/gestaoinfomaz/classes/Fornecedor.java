package br.com.gestaoinfomaz.classes;
import java.util.Date;

/**
 * Classe que representa um fornecedor no sistema de gestão Infomaz.
 * Armazena o identificador, nome e a data de cadastro do fornecedor.
 */
public class Fornecedor {

    // Identificador único do fornecedor
    private String idFornecedor;

    // Nome do fornecedor
    private String nomeFornecedor;

    // Data de cadastro do fornecedor no sistema
    private Date dataCadastro;

    /**
     * Construtor da classe Fornecedor.
     *
     * @param idFornecedor    Identificador do fornecedor
     * @param nomeFornecedor  Nome do fornecedor
     * @param dataCadastro    Data de cadastro do fornecedor
     */
    public Fornecedor(String idFornecedor, String nomeFornecedor, Date dataCadastro) {
        this.idFornecedor = idFornecedor;
        this.nomeFornecedor = nomeFornecedor;
        this.dataCadastro = dataCadastro;
    }

    /**
     * Retorna o identificador do fornecedor.
     *
     * @return idFornecedor
     */
    public String getIdFornecedor() {
        return idFornecedor;
    }

    /**
     * Retorna o nome do fornecedor.
     *
     * @return nomeFornecedor
     */
    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    /**
     * Retorna a data de cadastro do fornecedor.
     *
     * @return dataCadastro
     */
    public Date getDataCadastro() {
        return dataCadastro;
    }

    /**
     * Retorna uma representação textual dos dados do fornecedor.
     *
     * @return String formatada com ID, nome e data de cadastro
     */
    @Override
    public String toString() {
        return String.format("ID Fornecedor: %s, Nome: %s, Data Cadastro: %s",
                idFornecedor, nomeFornecedor, dataCadastro);
    }
}
