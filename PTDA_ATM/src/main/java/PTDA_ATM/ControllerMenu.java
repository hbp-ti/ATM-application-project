package PTDA_ATM;


import SQL.Query;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.image.ImageView;
import java.io.*;

/**
 * Controlador para a tela de menu principal.
 */
public class ControllerMenu {

    /**
     * Botão para efetuar logout.
     */
    @FXML
    private Button buttonLogOut;

    /**
     * Painel para a funcionalidade de levantamento.
     */
    @FXML
    private Pane buttonWithdraw;

    /**
     * Painel para a funcionalidade de consulta de saldo.
     */
    @FXML
    private Pane buttonBalance;

    /**
     * Painel para a funcionalidade de transferência.
     */
    @FXML
    private Pane buttonTransfer;

    /**
     * Painel para a funcionalidade de depósito.
     */
    @FXML
    private Pane buttonDeposit;

    /**
     * Painel para a funcionalidade de recarga de telemóvel.
     */
    @FXML
    private Pane buttonChargePhone;

    /**
     * Painel para a funcionalidade de pagamento.
     */
    @FXML
    private Pane buttonPayment;

    /**
     * Painel para a funcionalidade de extrato.
     */
    @FXML
    private Pane buttonMiniStatement;

    /**
     * Painel para a funcionalidade de alteração de PIN.
     */
    @FXML
    private Pane buttonChangePIN;

    /**
     * Painel para a funcionalidade de opções.
     */
    @FXML
    private Pane buttonOptions;

    /**
     * Rótulo de boas-vindas.
     */
    @FXML
    private Label labelWelcome;

    /**
     * Imagem de avatar masculino.
     */
    @FXML
    private ImageView maleAvatar;

    /**
     * Imagem de avatar feminino.
     */
    @FXML
    private ImageView femaleAvatar;

    /**
     * Imagem de avatar não especificado.
     */
    @FXML
    private ImageView otherAvatar;

    /**
     * Nome do cliente.
     */
    private String clientName;

    /**
     * Número do cartão do cliente.
     */
    private String clientCardNumber;

    /**
     * Objeto para executar consultas no banco de dados.
     */
    Query query = new Query();

    /**
     * Define o número do cartão do cliente.
     *
     * @param cardNumber O número do cartão do cliente.
     */
    public void setClientCardNumber(String cardNumber) {
        this.clientCardNumber = cardNumber;
        initialize();
    }

    /**
     * Define o nome do cliente.
     *
     * @param clientName O nome do cliente.
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
        initialize();
    }

    /**
     * Método de inicialização do controlador.
     * Configura os elementos da interface de user com base nos dados do cliente.
     * Configura os eventos de clique para os botões do menu.
     * Configura o cursor para os botões do menu.
     *
     */
    public void initialize() {
        if (clientName != null) {
            labelWelcome.setText("Welcome " + clientName);

            String gender = query.getGenderFromDatabase(clientCardNumber);

            if ("Male".equals(gender)) {
                maleAvatar.setVisible(true);
                femaleAvatar.setVisible(false);
                otherAvatar.setVisible(false);
            } else if ("Female".equals(gender)) {
                maleAvatar.setVisible(false);
                femaleAvatar.setVisible(true);
                otherAvatar.setVisible(false);
            } else {
                maleAvatar.setVisible(false);
                femaleAvatar.setVisible(false);
                otherAvatar.setVisible(true);
            }
        } else {
            labelWelcome.setText("Welcome");
        }

        buttonWithdraw.setOnMouseClicked(mouseEvent -> {
            try {
                switchToWithdraw(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonBalance.setOnMouseClicked(mouseEvent -> {
            try {
                switchToCheckBalance(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonTransfer.setOnMouseClicked(mouseEvent -> {
            try {
                switchToFundTransfer(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonDeposit.setOnMouseClicked(mouseEvent -> {
            try {
                switchToDeposit(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonChargePhone.setOnMouseClicked(mouseEvent -> {
            try {
                switchToChargePhone(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonPayment.setOnMouseClicked(mouseEvent -> {
            try {
                switchToMenuPayment(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonMiniStatement.setOnMouseClicked(mouseEvent -> {
            try {
                switchToMiniStatement(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonChangePIN.setOnMouseClicked(mouseEvent -> {
            try {
                switchToChangePIN(mouseEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        buttonOptions.setOnMouseClicked(mouseEvent -> {
            try {
                switchToOptions(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

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

        buttonChargePhone.setOnMouseEntered(e -> buttonChargePhone.setCursor(javafx.scene.Cursor.HAND));
        buttonChargePhone.setOnMouseExited(e -> buttonChargePhone.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonPayment.setOnMouseEntered(e -> buttonPayment.setCursor(javafx.scene.Cursor.HAND));
        buttonPayment.setOnMouseExited(e -> buttonPayment.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonMiniStatement.setOnMouseEntered(e -> buttonMiniStatement.setCursor(javafx.scene.Cursor.HAND));
        buttonMiniStatement.setOnMouseExited(e -> buttonMiniStatement.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonChangePIN.setOnMouseEntered(e -> buttonChangePIN.setCursor(javafx.scene.Cursor.HAND));
        buttonChangePIN.setOnMouseExited(e -> buttonChangePIN.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonOptions.setOnMouseEntered(e -> buttonOptions.setCursor(javafx.scene.Cursor.HAND));
        buttonOptions.setOnMouseExited(e -> buttonOptions.setCursor(javafx.scene.Cursor.DEFAULT));

    }

    /**
     * Troca para a tela de login.
     *
     * @param event O evento que acionou a ação.
     * @throws IOException Se houver um erro ao carregar a tela.
     */
    public void switchToLogIn(ActionEvent event) throws IOException {
        Stage stage = (Stage) buttonLogOut.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Troca para a tela de alteração de PIN.
     *
     * @param event O evento de mouse que acionou a ação.
     * @throws IOException Se houver um erro ao carregar a tela.
     */
    public void switchToChangePIN(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangePIN.fxml"));
        Parent root = loader.load();
        ControllerChangePIN controller = loader.getController();
        controller.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonChangePIN.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Troca para a tela de recarga do telemóvel.
     *
     * @param event O evento de mouse que acionou a ação.
     * @throws IOException Se houver um erro ao carregar a tela.
     */
    public void switchToChargePhone(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChargePhone.fxml"));
        Parent root = loader.load();
        ControllerChargePhone controller = loader.getController();
        controller.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonChargePhone.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Troca para a tela de verificação de saldo.
     *
     * @param event O evento de mouse que acionou a ação.
     * @throws IOException Se houver um erro ao carregar a tela.
     */
    public void switchToCheckBalance(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CheckBalance.fxml"));
        Parent root = loader.load();
        ControllerCheckBalance controller = loader.getController();
        controller.setClientCardNumber(clientCardNumber);
        controller.checkBalance();
        Stage stage = (Stage) buttonBalance.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Troca para a tela de depósito.
     *
     * @param event O evento de mouse que acionou a ação.
     * @throws IOException Se houver um erro ao carregar a tela.
     */
    public void switchToDeposit(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Deposit.fxml"));
        Parent root = loader.load();
        ControllerDeposit controller = loader.getController();
        controller.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonDeposit.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Troca para a tela de transferência de fundos.
     *
     * @param event O evento de mouse que acionou a ação.
     * @throws IOException Se houver um erro ao carregar a tela.
     */
    public void switchToFundTransfer(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FundTransfer.fxml"));
        Parent root = loader.load();
        ControllerFundTransfer controller = loader.getController();
        controller.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonTransfer.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Troca para a tela de pagamento.
     *
     * @param event O evento de mouse que acionou a ação.
     * @throws IOException Se houver um erro ao carregar a tela.
     */
    public void switchToMenuPayment(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuPayment.fxml"));
        Parent root = loader.load();
        ControllerMenuPayment controller = loader.getController();
        controller.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonPayment.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Troca para a tela de extrato.
     *
     * @param event O evento de mouse que acionou a ação.
     * @throws IOException Se houver um erro ao carregar a tela.
     */
    public void switchToMiniStatement(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MiniStatement.fxml"));
        Parent root = loader.load();
        ControllerMiniStatement controller = loader.getController();
        controller.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonMiniStatement.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Troca para a tela de opções.
     *
     * @param event O evento de mouse que acionou a ação.
     * @throws IOException Se houver um erro ao carregar a tela.
     */
    public void switchToOptions(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Options.fxml"));
        Parent root = loader.load();
        ControllerOptions controller = loader.getController();
        controller.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonOptions.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Troca para a tela de saque.
     *
     * @param event O evento de mouse que acionou a ação.
     * @throws IOException Se houver um erro ao carregar a tela.
     */
    public void switchToWithdraw(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Withdraw.fxml"));
        Parent root = loader.load();
        ControllerWithdraw controller = loader.getController();
        controller.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonWithdraw.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
