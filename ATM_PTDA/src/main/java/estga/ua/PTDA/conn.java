package estga.ua.PTDA;

import java.sql.*;
import java.util.Scanner;

public class conn {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("URL da base de dados: ");
        String url = sc.nextLine();

        System.out.println("Schema da base de dados: ");
        String schema = sc.nextLine();

        System.out.println("Nome de utilizador: ");
        String user = sc.nextLine();

        System.out.println("Password: ");
        String pass = sc.nextLine();

        try {
            conn connection = new conn();
            connection.connect(url, schema, user, pass);
        } catch(Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
    }
    public void connect(String url, String schema, String user, String password) {
        Connection c;
        Statement s;

        try  {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://" + url + ":3306/" + schema + "", user, password);
            s = c.createStatement();
            System.out.println("Conexão bem-sucedida!");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Erro de conexão: " + e.getMessage());
        }
    }
}
