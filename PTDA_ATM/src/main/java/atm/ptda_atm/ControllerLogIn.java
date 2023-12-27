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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerLogIn {

    @FXML
    private TextField cardNumberInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Button loginButton;

    @FXML
    private Label labelValidation;

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
    private String clientCardNumber;
    private String password;
    private String clientName;

    public void initialize() {
        cardNumberInput.setOnKeyTyped(event -> clearValidationErrors());
        passwordInput.setOnKeyTyped(event -> clearValidationErrors());

        loginButton.setOnMouseEntered(e -> loginButton.setCursor(javafx.scene.Cursor.HAND));
        loginButton.setOnMouseExited(e -> loginButton.setCursor(javafx.scene.Cursor.DEFAULT));

        signupLink.setOnMouseEntered(e -> signupLink.setCursor(javafx.scene.Cursor.HAND));
        signupLink.setOnMouseExited(e -> signupLink.setCursor(javafx.scene.Cursor.DEFAULT));
    }


    public void switchToMainPage(ActionEvent event) throws IOException {
        clientCardNumber = cardNumberInput.getText();
        password = passwordInput.getText();

        boolean verifyCard = verifyCardInfo(clientCardNumber,password);

        if (verifyCard) {
            boolean success = getClientName(clientCardNumber);

            if (success) {
                labelValidation.setText("Valid Data!");
                applyCorrectStyle();

                PauseTransition pauseValidation = new PauseTransition(Duration.seconds(2));
                pauseValidation.setOnFinished(events -> {
                    try {
                        switchToMenu(event);
                    } catch (IOException es) {
                        es.printStackTrace();
                    }
                });
                pauseValidation.play();
                return;
            }
        } else {
            labelValidation.setText("Invalid data!");
            passwordInput.setText("");
            applyValidationStyle();
        }
    }

    public boolean verifyCardInfo(String clientCardNumber, String password) {
        try {
            connection = Conn.getConnection();
            preparedStatement = connection.prepareStatement("SELECT cardNumber, cardPIN FROM Card WHERE cardNumber = ? AND cardPIN = ?");
            preparedStatement.setString(1, this.cardNumberInput.getText());
            preparedStatement.setString(2, this.passwordInput.getText());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return true;
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
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean getClientName(String clientCardNumber) {
        try {
            String query = "SELECT clientName FROM BankAccount WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber = ?)";
            preparedStatement2 = connection.prepareStatement(query);
            preparedStatement2.setString(1, clientCardNumber);
            rsName = preparedStatement2.executeQuery();

            if (rsName.next()) {
                this.clientName = rsName.getString("clientName");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsName != null) {
                    rsName.close();
                }
                if (preparedStatement2 != null) {
                    preparedStatement2.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return false;
    }

    public void switchToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = loader.load();
        ControllerMenu menuController = loader.getController();
        menuController.setClientCardNumber(clientCardNumber);
        menuController.setClientName(clientName);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
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

    // Método para aplicar o estilo de borda vermelho
    private void applyValidationStyle() {
        labelValidation.setTextFill(Color.RED);
        Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
        cardNumberInput.setBorder(border);
        passwordInput.setBorder(border);
    }

    private void applyCorrectStyle() {
        labelValidation.setTextFill(Color.GREEN);
        Border border = new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
        cardNumberInput.setBorder(border);
        passwordInput.setBorder(border);
    }

    private void clearValidationErrors() {
        labelValidation.setText("");
        cardNumberInput.setBorder(null);
        passwordInput.setBorder(null);
    }
}

