package PTDA_ATM;

import SQL.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;

public class ControllerCheckBalance {
    @FXML
    private Button buttonGoBack;

    @FXML
    private Label labelSaldo;

    private String clientCardNumber;
    Query query = new Query();



    public void initialize() {
        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(Cursor.DEFAULT));
    }

    public void setClientCardNumber(String clientCardNumber) {
        this.clientCardNumber = clientCardNumber;
        initialize();
    }

    public void checkBalance() {
        BigDecimal saldo = query.checkBalance(clientCardNumber);
        labelSaldo.setText("Balance: " + saldo+"€");
    }

    public void switchToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = loader.load();
        ControllerMenu menuController = loader.getController();
        String clientName = query.getClientName(clientCardNumber);
        menuController.setClientName(clientName);
        menuController.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonGoBack.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
