package PTDA_ATM;

import SQL.Conn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;

/**
 * A classe principal que inicia a aplicação ATM.
 */
public class Main extends Application {

    /**
     * Objeto de conexão com o banco de dados.
     */
    private Conn connection;


    /**
     * Retorna um objeto do tipo conexao
     *
     * @return O objeto da conexao.
     */
    public Conn getConnection() {
        return connection;
    }

    /**
     * Define o objeto da conexao.
     *
     * @return O objeto da conexao.
     */
    public void setConnection(Conn connection) {
        this.connection = connection;
    }

    /**
     * O método start é chamado quando a aplicação é iniciada.
     * Ele configura a interface gráfica do user e exibe a tela de login.
     *
     * @param stage O palco principal da aplicação.
     * @throws IOException Se houver um erro ao carregar o arquivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        connection = new Conn();
        connection.doConnection();
        try {
            if (connection.isConnected()) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LogIn.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 850, 600);
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/atm.png")));
                stage.setTitle("ATM");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                stage.centerOnScreen();
            } else {
                throw new SQLException("Erro de conexão!");
            }
        } catch (SQLException e) {
            System.out.println("SQLExeption: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (InputMismatchException e) {
            System.out.println("Credências da Bade de Dados Erradas: " + e.getMessage());
        }
    }

    /**
     * O método stop é chamado quando a aplicação é encerrada.
     * Ele fecha a conexão com o banco de dados antes de finalizar a aplicação.
     */
    @Override
    public void stop() {
        // Feche a conexão ao encerrar a aplicação
        if (connection != null && connection.isConnected()) {
            connection.close();
        }
    }

    /**
     * O método main é o ponto de entrada da aplicação.
     *
     * @param args Os argumentos da linha de comando.
     */
    public static void main(String[] args) {
        launch();
    }
}