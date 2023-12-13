package atm.ptda_atm;

import javafx.animation.PauseTransition;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerLogIn {


    @FXML
    private TextField cardNumberInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private Button loginButton;

    @FXML
    private Label labelValidacao;

    @FXML
    private Hyperlink signupLink;

    @FXML
    private ImageView bankLogo;

    private Button logIn;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToMainPage(ActionEvent event) throws IOException {
        Conn con = new Conn();
        con.doConnection();

        try (Connection connection = Conn.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT cardNumber, cardPIN FROM Card WHERE cardNumber = ? AND cardPIN = ?")) {

            preparedStatement.setInt(1, Integer.parseInt(this.cardNumberInput.getText()));
            preparedStatement.setString(2, this.passwordInput.getText());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                labelValidacao.setTextFill(Color.GREEN);
                labelValidacao.setText("Dados válidos!");

                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(events -> {
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                pause.play();

            } else {
                labelValidacao.setTextFill(Color.RED);
                labelValidacao.setText("Dados inválidos!");
            }

            preparedStatement.close();
            rs.close();
        } catch (SQLException e) {
            System.err.println("Erro de conexão: " + e.getMessage());
        }
    }

    public void switchToSignUp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
