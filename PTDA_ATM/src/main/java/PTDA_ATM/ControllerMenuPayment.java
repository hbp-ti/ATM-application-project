package PTDA_ATM;

import SQL.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controlador para a tela de menu de pagamentos.
 */
public class ControllerMenuPayment {

    /**
     * Painel para o botão de pagamento de serviços.
     */
    @FXML
    private Pane buttonServicePay;

    /**
     * Painel para o botão de pagamento de impostos ou contas.
     */
    @FXML
    private Pane buttonStatePay;

    /**
     * Botão para voltar ao menu principal.
     */
    @FXML
    private Button buttonGoBack;

    /**
     * Número da conta do cliente.
     */
    private String clientAccountNumber;

    /**
     * Nome do cliente.
     */
    private String clientName;

    /**
     * Objeto para executar consultas no banco de dados.
     */
    private final Query query = new Query();

    /**
     * Objeto para criar sombra para os botões
     */
    DropShadow shadow = new DropShadow();

    /**
     * Inicializa o controlador.
     */
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

        buttonGoBack.setOnMouseEntered(e -> {
            buttonGoBack.setCursor(Cursor.HAND);
            buttonGoBack.setTranslateY(2);
            buttonGoBack.setEffect(shadow);
            buttonGoBack.setStyle("-fx-background-color: #761215; -fx-background-radius: 8;");
        });
        buttonGoBack.setOnMouseExited(e -> {
            buttonGoBack.setCursor(Cursor.DEFAULT);
            buttonGoBack.setTranslateY(0);
            buttonGoBack.setEffect(null);
            buttonGoBack.setStyle("-fx-background-color: #B01B1F; -fx-background-radius: 8;");
        });

        buttonServicePay.setOnMouseEntered(e -> {
            buttonServicePay.setCursor(javafx.scene.Cursor.HAND);
            buttonServicePay.setTranslateY(2);
            buttonServicePay.setEffect(shadow);
            buttonServicePay.setStyle("-fx-background-color: #00447F; -fx-background-radius: 15;");
        });
        buttonServicePay.setOnMouseExited(e -> {
            buttonServicePay.setCursor(javafx.scene.Cursor.DEFAULT);
            buttonServicePay.setTranslateY(0);
            buttonServicePay.setEffect(null);
            buttonServicePay.setStyle("-fx-background-color:  #0C72D7; -fx-background-radius: 15;");
        });

        buttonStatePay.setOnMouseEntered(e -> {
            buttonStatePay.setCursor(javafx.scene.Cursor.HAND);
            buttonStatePay.setTranslateY(2);
            buttonStatePay.setEffect(shadow);
            buttonStatePay.setStyle("-fx-background-color: #5C2018; -fx-background-radius: 15;");
        });
        buttonStatePay.setOnMouseExited(e -> {
            buttonStatePay.setCursor(javafx.scene.Cursor.DEFAULT);
            buttonStatePay.setTranslateY(0);
            buttonStatePay.setEffect(null);
            buttonStatePay.setStyle("-fx-background-color:  #973528; -fx-background-radius: 15;");
        });
    }

    /**
     * Define o número da conta do cliente.
     *
     * @param clientAccountNumber Número da conta do cliente.
     */
    public void setClientAccountNumber(String clientAccountNumber) {
        this.clientAccountNumber = clientAccountNumber;
        initialize();
    }

    /**
     * Define o nome do cliente.
     *
     * @param clientName Nome do cliente.
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * Alterna para a tela do menu principal.
     *
     * @param event O evento associado à ação.
     * @throws IOException Exceção de entrada/saída.
     */
    public void switchToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = loader.load();
        ControllerMenu menuController = loader.getController();
        String clientName = query.getClientName(clientAccountNumber);
        menuController.setClientName(clientName);
        menuController.setClientAccountNumber(clientAccountNumber);
        Stage stage = (Stage) buttonGoBack.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Alterna para a tela de pagamento de serviços.
     *
     * @param event O evento associado à ação.
     * @throws IOException Exceção de entrada/saída.
     */
    public void switchToServicePayment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ServicePayment.fxml"));
        Parent root = loader.load();
        ControllerServicePayment controller = loader.getController();
        controller.setClientAccountNumber(clientAccountNumber);
        Stage stage = (Stage) buttonServicePay.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Alterna para a tela de pagamento de impostos ou contas.
     *
     * @param event O evento associado à ação.
     * @throws IOException Exceção de entrada/saída.
     */
    public void switchToTheStatePayment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TheStatePayment.fxml"));
        Parent root = loader.load();
        ControllerTheStatePayment controller = loader.getController();
        controller.setClientAccountNumber(clientAccountNumber);
        Stage stage = (Stage) buttonStatePay.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
