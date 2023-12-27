package PTDA_ATM;

import SQL.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;


public class ControllerMenuPayment {

    @FXML
    private Pane buttonServicePay;

    @FXML
    private Pane buttonStatePay;

    @FXML
    private Button buttonGoBack;

    private String clientCardNumber;
    private String clientName;
    Query query = new Query();

    public void initialize() {
        buttonServicePay.setOnMouseClicked(mouseEvent -> {
            try {
                switchToServicePayment(new ActionEvent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonStatePay.setOnMouseClicked(mouseEvent -> {
            try {
                switchToTheStatePayment(new ActionEvent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(javafx.scene.Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonServicePay.setOnMouseEntered(e -> buttonServicePay.setCursor(javafx.scene.Cursor.HAND));
        buttonServicePay.setOnMouseExited(e -> buttonServicePay.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonStatePay.setOnMouseEntered(e -> buttonStatePay.setCursor(javafx.scene.Cursor.HAND));
        buttonStatePay.setOnMouseExited(e -> buttonStatePay.setCursor(javafx.scene.Cursor.DEFAULT));
    }

    public void setClientCardNumber(String clientCardNumber) {
        this.clientCardNumber = clientCardNumber;
        initialize();
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public void switchToServicePayment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ServicePayment.fxml"));
        Parent root = loader.load();
        ControllerServicePayment controller = loader.getController();
        controller.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonServicePay.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTheStatePayment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TheStatePayment.fxml"));
        Parent root = loader.load();
        ControllerTheStatePayment controller = loader.getController();
        controller.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonStatePay.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
