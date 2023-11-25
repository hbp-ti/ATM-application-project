package estga.ua.PTDA;

import java.sql.*;
import java.util.Scanner;

public class conn {
    private Connection c;
    private Statement s;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("URL da base de dados: ");
        String url = sc.nextLine();

        System.out.println("Porta: ");
        int porta = sc.nextInt();
        sc.nextLine();

        System.out.println("Schema da base de dados: ");
        String schema = sc.nextLine();

        System.out.println("Nome de utilizador: ");
        String user = sc.nextLine();

        System.out.println("Password: ");
        String pass = sc.nextLine();

        try {
            conn connection = new conn();
            connection.connect(url, porta, schema, user, pass);

            if (connection.isConnected()) {
                String query = "SELECT clientName FROM BankAccount WHERE accountNumber = 12345678";

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Processa os resultados da consulta
                while (resultSet.next()) {
                    String clientName = resultSet.getString("clientName");
                    System.out.println(clientName);
                }

                // Feche os recursos utilizados
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (Exception e) {
            System.out.println("Erro ao conectar ou executar a consulta: " + e.getMessage());
        }
    }

    public void connect(String url, int porta, String schema, String user, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://" + url + ":" + porta + "/" + schema, user, password);
            s = c.createStatement();
            System.out.println("Conexão bem-sucedida!");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Erro de conexão: " + e.getMessage());
        }
    }

    public Statement createStatement() {
        return s;
    }

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

    public boolean isConnected() {
        return c != null;
    }
}
