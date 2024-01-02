package SQL;

import java.sql.*;
import java.util.Scanner;

/**
 * A classe Conn é responsável por tratar a conexão com a base de dados MySQL.
 */
public class Conn {
    /**
     * Conexão estática com o banco de dados.
     */
    private static Connection c;

    /**
     * Declaração utilizada para executar consultas SQL.
     */
    private Statement s;

    /**
     * Inicia uma nova conexão com a base de dados.
     * Se já existir uma conexão, exibe uma mensagem informando sobre a conexão existente.
     */
    public void doConnection() {
        if (!isConnected()) {
            Scanner sc = new Scanner(System.in);

            System.out.println("Host da base de dados:");
            String url = sc.nextLine();

            System.out.println("Porta:");
            int port = sc.nextInt();
            sc.nextLine();

            System.out.println("Schema da base de dados:");
            String schema = sc.nextLine();

            System.out.println("Nome de utilizador:");
            String user = sc.nextLine();

            System.out.println("Password:");
            String pass = sc.nextLine();

            connect(url, port, schema, user, pass);
        } else {
            System.out.println("Já existe uma conexão estabelecida.");
        }
    }

    /**
     * Realiza a conexão com a base de dados MySQL.
     *
     * @param url      URL do host da BD.
     * @param port     Porta da BD.
     * @param schema   Nome do esquema da BD.
     * @param user     Nome de utilizador.
     * @param password Senha para autenticação.
     */
    private void connect(String url, int port, String schema, String user, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://" + url + ":" + port + "/" + schema, user, password);
            s = c.createStatement();
            System.out.println("Conexão bem-sucedida!");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Erro de conexão: " + e.getMessage());
        }
    }

    /**
     * Obtém a conexão ativa com a base de dados.
     *
     * @return A conexão com a base de dados.
     */
    public static Connection getConnection() {
        return c;
    }

    /**
     * Fecha a conexão com a base de dados.
     * Fecha também o Statement associado, se estiver aberto.
     */
    public void close() {
        try {
            if (s != null) {
                s.close();
            }
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }

    /**
     * Verifica se há uma conexão ativa com a base de dados.
     *
     * @return True se houver uma conexão ativa; false, caso contrário.
     */
    public boolean isConnected() {
        return c != null;
    }
}
