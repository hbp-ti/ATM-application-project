package PTDA_ATM;


import SQL.Query;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
     * Número da conta do cliente.
     */
    private String clientAccountNumber;

    /**
     * Objeto para executar consultas no banco de dados.
     */
    Query query = new Query();

    /**
     * Objeto para criar sombra para os botões
     */
    DropShadow shadow = new DropShadow();

    /**
     * Define o número da conta do cliente.
     *
     * @param accountNumber O número da conta do cliente.
     */
    public void setClientAccountNumber(String accountNumber) {
        this.clientAccountNumber = accountNumber;
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
     * Configura os estilos dos botões
     */
    public void initialize() {
        if (clientName != null) {
            labelWelcome.setText("Welcome " + clientName);

            String gender = query.getGenderFromDatabase(clientAccountNumber);

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


        buttonLogOut.setOnMouseEntered(e -> {
            buttonLogOut.setCursor(javafx.scene.Cursor.HAND);
            buttonLogOut.setTranslateY(2);
            buttonLogOut.setEffect(shadow);
            buttonLogOut.setStyle("-fx-background-color: #761215; -fx-background-radius: 8;");
        });

        buttonLogOut.setOnMouseExited(e -> {
            buttonLogOut.setCursor(javafx.scene.Cursor.DEFAULT);
            buttonLogOut.setTranslateY(0);
            buttonLogOut.setEffect(null);
            buttonLogOut.setStyle("-fx-background-color: #B01B1F; -fx-background-radius: 8;");
        });

        buttonWithdraw.setOnMouseEntered(e -> {
            buttonWithdraw.setCursor(javafx.scene.Cursor.HAND);
            buttonWithdraw.setTranslateY(2);
            buttonWithdraw.setEffect(shadow);
            buttonWithdraw.setStyle("-fx-background-color: #00447F; -fx-background-radius: 15;");
        });
        buttonWithdraw.setOnMouseExited(e -> {
            buttonWithdraw.setCursor(javafx.scene.Cursor.DEFAULT);
            buttonWithdraw.setTranslateY(0);
            buttonWithdraw.setEffect(null);
            buttonWithdraw.setStyle("-fx-background-color:  #0C72D7; -fx-background-radius: 15;");
        });

        buttonBalance.setOnMouseEntered(e -> {
            buttonBalance.setCursor(javafx.scene.Cursor.HAND);
            buttonBalance.setTranslateY(2);
            buttonBalance.setEffect(shadow);
            buttonBalance.setStyle("-fx-background-color: #17515A; -fx-background-radius: 15;");
        });
        buttonBalance.setOnMouseExited(e -> {
            buttonBalance.setCursor(javafx.scene.Cursor.DEFAULT);
            buttonBalance.setTranslateY(0);
            buttonBalance.setEffect(null);
            buttonBalance.setStyle("-fx-background-color: #278795; -fx-background-radius: 15;");
        });

        buttonTransfer.setOnMouseEntered(e -> {
            buttonTransfer.setCursor(javafx.scene.Cursor.HAND);
            buttonTransfer.setTranslateY(2);
            buttonTransfer.setEffect(shadow);
            buttonTransfer.setStyle("-fx-background-color: #5C2018; -fx-background-radius: 15;");
        });
        buttonTransfer.setOnMouseExited(e -> {
            buttonTransfer.setCursor(javafx.scene.Cursor.DEFAULT);
            buttonTransfer.setTranslateY(0);
            buttonTransfer.setEffect(null);
            buttonTransfer.setStyle("-fx-background-color: #973528; -fx-background-radius: 15;");
        });

        buttonDeposit.setOnMouseEntered(e -> {
            buttonDeposit.setCursor(javafx.scene.Cursor.HAND);
            buttonDeposit.setTranslateY(2);
            buttonDeposit.setEffect(shadow);
            buttonDeposit.setStyle("-fx-background-color: #102D69; -fx-background-radius: 15;");
        });
        buttonDeposit.setOnMouseExited(e -> {
            buttonDeposit.setCursor(javafx.scene.Cursor.DEFAULT);
            buttonDeposit.setTranslateY(0);
            buttonDeposit.setEffect(null);
            buttonDeposit.setStyle("-fx-background-color: #1948A4; -fx-background-radius: 15;");
        });

        buttonChargePhone.setOnMouseEntered(e -> {
            buttonChargePhone.setCursor(javafx.scene.Cursor.HAND);
            buttonChargePhone.setTranslateY(2);
            buttonChargePhone.setEffect(shadow);
            buttonChargePhone.setStyle("-fx-background-color: #0E5F50; -fx-background-radius: 15;");
        });
        buttonChargePhone.setOnMouseExited(e -> {
            buttonChargePhone.setCursor(javafx.scene.Cursor.DEFAULT);
            buttonChargePhone.setTranslateY(0);
            buttonChargePhone.setEffect(null);
            buttonChargePhone.setStyle("-fx-background-color: #179981; -fx-background-radius: 15;");
        });

        buttonPayment.setOnMouseEntered(e -> {
            buttonPayment.setCursor(javafx.scene.Cursor.HAND);
            buttonPayment.setTranslateY(2);
            buttonPayment.setEffect(shadow);
            buttonPayment.setStyle("-fx-background-color: #6E300C; -fx-background-radius: 15;");
        });
        buttonPayment.setOnMouseExited(e -> {
            buttonPayment.setCursor(javafx.scene.Cursor.DEFAULT);
            buttonPayment.setTranslateY(0);
            buttonPayment.setEffect(null);
            buttonPayment.setStyle("-fx-background-color:  #A84913; -fx-background-radius: 15;");
        });

        buttonMiniStatement.setOnMouseEntered(e -> {
            buttonMiniStatement.setCursor(javafx.scene.Cursor.HAND);
            buttonMiniStatement.setTranslateY(2);
            buttonMiniStatement.setEffect(shadow);
            buttonMiniStatement.setStyle("-fx-background-color:  #6B1A2A; -fx-background-radius: 15;");
        });
        buttonMiniStatement.setOnMouseExited(e -> {
            buttonMiniStatement.setCursor(javafx.scene.Cursor.DEFAULT);
            buttonMiniStatement.setTranslateY(0);
            buttonMiniStatement.setEffect(null);
            buttonMiniStatement.setStyle("-fx-background-color: null; -fx-border-color:  #6B1A2A; -fx-border-style: solid; -fx-border-width: 3; -fx-border-radius: 15");
        });

        buttonChangePIN.setOnMouseEntered(e -> {
            buttonChangePIN.setCursor(javafx.scene.Cursor.HAND);
            buttonChangePIN.setTranslateY(2);
            buttonChangePIN.setEffect(shadow);
            buttonChangePIN.setStyle("-fx-background-color:  #302C2B; -fx-background-radius: 15;");
        });
        buttonChangePIN.setOnMouseExited(e -> {
            buttonChangePIN.setCursor(javafx.scene.Cursor.DEFAULT);
            buttonChangePIN.setTranslateY(0);
            buttonChangePIN.setEffect(null);
            buttonChangePIN.setStyle("-fx-background-color: null; -fx-border-color:  #302C2B; -fx-border-style: solid; -fx-border-width: 3; -fx-border-radius: 15");
        });

        buttonOptions.setOnMouseEntered(e -> {
            buttonOptions.setCursor(javafx.scene.Cursor.HAND);
            buttonOptions.setTranslateY(2);
            buttonOptions.setEffect(shadow);
            buttonOptions.setStyle("-fx-background-color:  #372170; -fx-background-radius: 15;");
        });
        buttonOptions.setOnMouseExited(e -> {
            buttonOptions.setCursor(javafx.scene.Cursor.DEFAULT);
            buttonOptions.setTranslateY(0);
            buttonOptions.setEffect(null);
            buttonOptions.setStyle("-fx-background-color: null; -fx-border-color:  #372170; -fx-border-style: solid; -fx-border-width: 3; -fx-border-radius: 15");
        });

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
        controller.setClientAccountNumber(clientAccountNumber);
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
        controller.setClientAccountNumber(clientAccountNumber);
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
        controller.setClientAccountNumber(clientAccountNumber);
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
        controller.setClientAccountNumber(clientAccountNumber);
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
        controller.setClientAccountNumber(clientAccountNumber);
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
        controller.setClientAccountNumber(clientAccountNumber);
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
        controller.setClientAccountNumber(clientAccountNumber);
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
        controller.setClientAccountNumber(clientAccountNumber);
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
        controller.setClientAccountNumber(clientAccountNumber);
        Stage stage = (Stage) buttonWithdraw.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
