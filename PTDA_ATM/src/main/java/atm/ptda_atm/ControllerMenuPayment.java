package atm.ptda_atm;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class ControllerMenuPayment {

    @FXML
    private Pane buttonServicePay;

    @FXML
    private Pane buttonStatePay;

    @FXML
    private Button buttonGoBack;

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


    public void switchToMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) buttonGoBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToServicePayment(ActionEvent event) throws IOException {
        Stage stage = (Stage) buttonServicePay.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("ServicePayment.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTheStatePayment(ActionEvent event) throws IOException {
        Stage stage = (Stage) buttonStatePay.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("TheStatePayment.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
