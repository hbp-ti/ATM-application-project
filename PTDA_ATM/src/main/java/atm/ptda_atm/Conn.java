package atm.ptda_atm;

import java.sql.*;
import java.util.Scanner;

public class Conn {
    private Connection c;
    private Statement s;

    public Connection doConnection() {
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
        return c;
    }

    public void connect(String url, int port, String schema, String user, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://" + url + ":" + port + "/" + schema, user, password);
            s = c.createStatement();
            System.out.println("Conexão bem-sucedida!");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Erro de conexão: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return c;
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
