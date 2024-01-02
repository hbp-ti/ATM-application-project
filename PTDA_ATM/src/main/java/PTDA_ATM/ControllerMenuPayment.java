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
     * Número do cartão do cliente.
     */
    private String clientCardNumber;

    /**
     * Nome do cliente.
     */
    private String clientName;

    /**
     * Objeto para executar consultas no banco de dados.
     */
    private final Query query = new Query();

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

        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(javafx.scene.Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonServicePay.setOnMouseEntered(e -> buttonServicePay.setCursor(javafx.scene.Cursor.HAND));
        buttonServicePay.setOnMouseExited(e -> buttonServicePay.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonStatePay.setOnMouseEntered(e -> buttonStatePay.setCursor(javafx.scene.Cursor.HAND));
        buttonStatePay.setOnMouseExited(e -> buttonStatePay.setCursor(javafx.scene.Cursor.DEFAULT));
    }

    /**
     * Define o número do cartão do cliente.
     *
     * @param clientCardNumber Número do cartão do cliente.
     */
    public void setClientCardNumber(String clientCardNumber) {
        this.clientCardNumber = clientCardNumber;
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
        String clientName = query.getClientName(clientCardNumber);
        menuController.setClientName(clientName);
        menuController.setClientCardNumber(clientCardNumber);
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
        controller.setClientCardNumber(clientCardNumber);
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
        controller.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonStatePay.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
