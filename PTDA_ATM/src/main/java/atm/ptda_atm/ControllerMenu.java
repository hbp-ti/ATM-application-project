package atm.ptda_atm;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.io.*;

public class ControllerMenu {

    @FXML
    private AnchorPane buttonLogOut;

    @FXML
    private Pane buttonWithdraw;

    @FXML
    private Pane buttonBalance;

    @FXML
    private Pane buttonTransfer;

    @FXML
    private Pane buttonDeposit;

    @FXML
    private Pane buttonChangePIN;

    @FXML
    private Pane buttonPayment;

    @FXML
    private Pane buttonMiniStatement;

    @FXML
    private Label labelWelcome;

    private String clientName;

    public void setClientName(String name) {
        this.clientName = name;
        initialize();
    }

    public void initialize() {
        if (clientName != null) {
            labelWelcome.setText("Welcome " + clientName);
        } else {
            labelWelcome.setText("Welcome");
        }

        buttonLogOut.setOnMouseClicked(mouseEvent -> {
            try {
                // Criando um ActionEvent vazio
                ActionEvent actionEvent = new ActionEvent();
                switchToLogIn(actionEvent);
            } catch (IOException e) {
                e.printStackTrace(); // Lida com exceções, você pode modificar conforme necessário
            }
        });

        buttonLogOut.setOnMouseEntered(e -> buttonLogOut.setCursor(javafx.scene.Cursor.HAND));
        buttonLogOut.setOnMouseExited(e -> buttonLogOut.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonLogOut.setOnMouseEntered(e -> buttonLogOut.setCursor(javafx.scene.Cursor.HAND));
        buttonLogOut.setOnMouseExited(e -> buttonLogOut.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonWithdraw.setOnMouseEntered(e -> buttonWithdraw.setCursor(javafx.scene.Cursor.HAND));
        buttonWithdraw.setOnMouseExited(e -> buttonWithdraw.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonBalance.setOnMouseEntered(e -> buttonBalance.setCursor(javafx.scene.Cursor.HAND));
        buttonBalance.setOnMouseExited(e -> buttonBalance.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonTransfer.setOnMouseEntered(e -> buttonTransfer.setCursor(javafx.scene.Cursor.HAND));
        buttonTransfer.setOnMouseExited(e -> buttonTransfer.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonDeposit.setOnMouseEntered(e -> buttonDeposit.setCursor(javafx.scene.Cursor.HAND));
        buttonDeposit.setOnMouseExited(e -> buttonDeposit.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonChangePIN.setOnMouseEntered(e -> buttonChangePIN.setCursor(javafx.scene.Cursor.HAND));
        buttonChangePIN.setOnMouseExited(e -> buttonChangePIN.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonPayment.setOnMouseEntered(e -> buttonPayment.setCursor(javafx.scene.Cursor.HAND));
        buttonPayment.setOnMouseExited(e -> buttonPayment.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonMiniStatement.setOnMouseEntered(e -> buttonMiniStatement.setCursor(javafx.scene.Cursor.HAND));
        buttonMiniStatement.setOnMouseExited(e -> buttonMiniStatement.setCursor(javafx.scene.Cursor.DEFAULT));

    }

    public void switchToLogIn(ActionEvent event) throws IOException {
        Stage stage = (Stage) buttonLogOut.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
