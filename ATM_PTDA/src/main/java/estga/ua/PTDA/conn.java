package estga.ua.PTDA;
import java.sql.*;

public class conn {
    public static void main(String[] args) {
        conn con = new conn();
        con.connect();
    }
    public void connect() {
        String url = "jdbc:mysql://estga-dev.ua.pt:3306/PTDA_BD_003";
        String user = "PTDA_003";
        String password = "Gos_493ft";
        Connection c;
        Statement s;

        try  {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(url, user, password);
            System.out.println("Conexão bem-sucedida!");
            s = c.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Erro de conexão: " + e.getMessage());
        }
    }
}
