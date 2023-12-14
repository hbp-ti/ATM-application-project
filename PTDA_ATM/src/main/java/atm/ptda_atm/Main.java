package atm.ptda_atm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;

public class Main extends Application {

    private Conn connection;

    @Override
    public void start(Stage stage) throws IOException {
        connection = new Conn();
        connection.doConnection();
        try {
            if (connection.isConnected()) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LogIn.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 850, 600);
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/atm.png")));
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

    @Override
    public void stop() {
        // Feche a conexão ao encerrar a aplicação
        if (connection != null && connection.isConnected()) {
            connection.close();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}