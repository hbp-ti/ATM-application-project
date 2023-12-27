package PTDA_ATM;

import SQL.Query;
import javafx.animation.PauseTransition;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.*;


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

    private Stage stage;
    private Scene scene;

    private String clientCardNumber;
    private String password;
    private String clientName;
    Query query = new Query();

    public void initialize() {
        cardNumberInput.setOnKeyTyped(event -> clearValidationErrors());
        passwordInput.setOnKeyTyped(event -> clearValidationErrors());

        loginButton.setOnMouseEntered(e -> loginButton.setCursor(javafx.scene.Cursor.HAND));
        loginButton.setOnMouseExited(e -> loginButton.setCursor(javafx.scene.Cursor.DEFAULT));

        signupLink.setOnMouseEntered(e -> signupLink.setCursor(javafx.scene.Cursor.HAND));
        signupLink.setOnMouseExited(e -> signupLink.setCursor(javafx.scene.Cursor.DEFAULT));
    }


    public void switchToMainPage(ActionEvent event){
        clientCardNumber = cardNumberInput.getText();
        password = passwordInput.getText();

        boolean verifyCard = query.verifyCardInfo(clientCardNumber,password);

        if (verifyCard) {
                labelValidation.setText("Valid Data!");
                applyCorrectStyle();
                this.clientName = query.getClientName(clientCardNumber);

                PauseTransition pauseValidation = new PauseTransition(Duration.seconds(2));
                pauseValidation.setOnFinished(events -> {
                    try {
                        switchToMenu(event);
                    } catch (IOException es) {
                        es.printStackTrace();
                    }
                });
                pauseValidation.play();
        } else {
            labelValidation.setText("Invalid data!");
            passwordInput.setText("");
            applyValidationStyle();
        }
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

