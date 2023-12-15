package atm.ptda_atm;

import javafx.animation.PauseTransition;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerLogIn {

    @FXML
    private TextField cardNumberInput;

    @FXML
    private PasswordField passwordInput;

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
    private PreparedStatement preparedStatement;
    private PreparedStatement preparedStatement2;
    private ResultSet rs;
    private ResultSet rsName;
    private Connection connection;

    public void initialize() {
        cardNumberInput.setOnKeyTyped(event -> clearValidationErrors());
        passwordInput.setOnKeyTyped(event -> clearValidationErrors());

        loginButton.setOnMouseEntered(e -> loginButton.setCursor(javafx.scene.Cursor.HAND));
        loginButton.setOnMouseExited(e -> loginButton.setCursor(javafx.scene.Cursor.DEFAULT));

        signupLink.setOnMouseEntered(e -> signupLink.setCursor(javafx.scene.Cursor.HAND));
        signupLink.setOnMouseExited(e -> signupLink.setCursor(javafx.scene.Cursor.DEFAULT));
    }

    private void clearValidationErrors() {
        labelValidacao.setText("");
        cardNumberInput.setBorder(null);
        passwordInput.setBorder(null);
    }

    public void switchToMainPage(ActionEvent event) throws IOException {
        try {
            connection = Conn.getConnection();
            preparedStatement = connection.prepareStatement("SELECT cardNumber, cardPIN FROM Card WHERE cardNumber = ? AND cardPIN = ?");
            preparedStatement.setString(1, this.cardNumberInput.getText());
            preparedStatement.setString(2, this.passwordInput.getText());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                preparedStatement2 = connection.prepareStatement("SELECT clientName FROM BankAccount WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber = ?)");
                preparedStatement2.setString(1, this.cardNumberInput.getText());

                rsName = preparedStatement2.executeQuery();

                if (rsName.next()) {
                    String nomeCliente = rsName.getString("clientName");

                    labelValidacao.setTextFill(Color.GREEN);
                    labelValidacao.setText("Dados válidos!");
                    Border border = new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
                    cardNumberInput.setBorder(border);
                    passwordInput.setBorder(border);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
                    Parent root = loader.load();
                    ControllerMenu menuController = loader.getController();
                    menuController.setClientName(nomeCliente);

                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(events -> {
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                        stage.centerOnScreen();
                    });
                    pause.play();

                    return;
                }
            } else {
                labelValidacao.setTextFill(Color.RED);
                labelValidacao.setText("Dados inválidos!");
                passwordInput.setText("");
                Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
                cardNumberInput.setBorder(border);
                passwordInput.setBorder(border);
            }

        } catch (SQLException e) {
            System.out.println("SQLExeption: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (rsName != null) {
                    rsName.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (preparedStatement2 != null) {
                    preparedStatement2.close();
                }

            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    public void switchToSignUp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
    }
}

