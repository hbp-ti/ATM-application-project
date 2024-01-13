package PTDA_ATM;

import SQL.Query;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import javafx.util.Duration;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Random;

/**
 * Controlador para a funcionalidade de transferência de fundos entre contas.
 */
public class ControllerFundTransfer {

    /**
     * Campo de texto para o número da conta de destino da transferência.
     */
    @FXML
    private TextField targetAccountNumber;

    /**
     * Campo de texto para o valor da transferência.
     */
    @FXML
    private TextField transferAmount;

    /**
     * Barra de progresso para exibir o progresso da transferência.
     */
    @FXML
    private ProgressBar progressTransfer;

    /**
     * Botão para realizar a transferência.
     */
    @FXML
    private Button buttonTransfer;

    /**
     * Botão para voltar ao menu principal.
     */
    @FXML
    private Button buttonGoBack;

    /**
     * Rótulo para exibir mensagens de validação ou sucesso.
     */
    @FXML
    private Label labelValidation;

    /**
     * Número da conta de origem da transferência.
     */
    private String sourceAccountNumber;

    /**
     * Objeto para executar consultas no banco de dados.
     */
    Query query = new Query();

    /**
     * Objeto para criar sombra para os botões
     */
    DropShadow shadow = new DropShadow();

    /**
     * Define o número do cartão do cliente de origem.
     *
     * @param sourceAccountNumber Número da conta do cliente de origem.
     */
    public void setClientAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
        initialize();
    }

    /**
     * Inicializa o controlador.
     */
    public void initialize() {
        targetAccountNumber.setOnKeyTyped(event -> clearValidationStyles());
        transferAmount.setOnKeyTyped(event -> clearValidationStyles());

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

        Reflection reflection = new Reflection();
        reflection.setFraction(0.5); // Ajusta o efeito de reflexão

        buttonTransfer.setOnMouseEntered(e -> {
            buttonTransfer.setCursor(Cursor.HAND);
            buttonTransfer.setTranslateY(2); // Altere o valor conforme necessário
            buttonTransfer.setEffect(reflection); // Adiciona reflexo ao passar o mouse
        });
        buttonTransfer.setOnMouseExited(e -> {
            buttonTransfer.setCursor(Cursor.DEFAULT);
            buttonTransfer.setTranslateY(0); // Retorna à posição original
            buttonTransfer.setEffect(null); // Remove o efeito de sombra ao passar o mouse
        });
    }

    /**
     * Realiza a lógica de transferência de fundos.
     *
     * @param event O evento associado à ação.
     * @throws IOException Exceção de entrada/saída.
     */
    public void transfer(ActionEvent event) throws IOException {
        String targetAccount = targetAccountNumber.getText();
        String transferAmountInput = transferAmount.getText();

        if (!validateInput(targetAccount, transferAmountInput)) {
            labelValidation.setText("Invalid input. Check and try again.");
            applyValidationStyle();
        } else {
            // Verificar se o número da conta de destino existe
            if (!query.doesAccountExist(targetAccount)) {
                showError("Target card does not exist. Please enter a valid one.");
                return; // Retorna sem realizar a transferência
            }

            float transferAmount = Float.parseFloat(transferAmountInput);

            if (transferAmount > 10000) {
                labelValidation.setText("Transfer amount exceeds the limit of 10000€");
                applyValidationStyle();
            } else {
                // Restante da lógica permanece igual
                float availableBalance = query.getAvailableBalance(sourceAccountNumber);
                if (transferAmount > availableBalance) {
                    labelValidation.setText("Insufficient funds");
                    applyValidationStyle();
                } else {
                    progressTransfer.setProgress(0.0);
                    Duration duration = Duration.seconds(3);
                    KeyFrame keyFrame = new KeyFrame(duration, new KeyValue(progressTransfer.progressProperty(), 1.0));
                    Timeline timeline = new Timeline(keyFrame);
                    timeline.setCycleCount(1);
                    timeline.play();
                    timeline.setOnFinished(e -> {
                        boolean success = false;
                        try {
                            success = performFundTransfer(sourceAccountNumber, targetAccount, transferAmount);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }

                        if (success) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();

                            labelValidation.setText(transferAmountInput + "€ has been withdrawn from your account!");
                            labelValidation.setTextFill(Color.GREEN);

                            String recipientEmail = query.getClientEmail(sourceAccountNumber);
                            String subject = "Transfer";
                            String message = "Subject: Transfer Notification\n" +
                                    "Dear "+query.getClientName(sourceAccountNumber)+",\n" +
                                    "We are pleased to inform you that a transfer of "+ transferAmountInput +"€ has been successfully made from your account. This transfer was processed on "+ formatter.format(now) +".\n" +
                                    "Should you have any questions or need further clarification, please do not hesitate to reach out to us. We are here to assist you.\n" +
                                    "Best regards,\n" +
                                    "ByteBank";
                            sendEmail(recipientEmail, subject, message);

                            // Não é necessário chamar movement novamente aqui

                            String recipientEmailTarget = query.getClientEmail(targetAccount);
                            String subjectTarget = "Transfer";

                            String messageTarget = "Subject: Transfer Notification\n"+
                                    "Dear "+query.getClientName(targetAccount)+",\n" +
                                    "We are pleased to inform you that a transfer of "+ transferAmountInput +"€ has been successfully made to your account. This transfer was processed on "+ formatter.format(now) +".\n" +
                                    "Should you have any questions or need further clarification, please do not hesitate to reach out to us. We are here to assist you.\n" +
                                    "Best regards,\n" +
                                    "ByteBank";
                            sendEmail(recipientEmailTarget, subjectTarget, messageTarget);

                            PauseTransition pause = new PauseTransition(Duration.seconds(3));
                            pause.setOnFinished(events -> {
                                try {
                                    switchToMenu(event);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                            pause.play();
                        }
                    });
                }
            }
        }
    }


    /**
     * Executa a lógica de transferência de fundos no banco de dados.
     *
     * @param sourceAccount   Número da conta de origem.
     * @param targetAccount   Número da conta de destino.
     * @param amount       Valor da transferência.
     * @return True se a transferência for bem-sucedida, False caso contrário.
     * @throws SQLException Exceção de SQL.
     */
    private boolean performFundTransfer(String sourceAccount, String targetAccount, float amount) throws SQLException {
        // Verifica se a conta de origem tem saldo suficiente
        float sourceBalance = query.getAvailableBalance(sourceAccount);
        if (sourceBalance < amount) {
            return false; // Saldo insuficiente
        }

        // Executa a lógica de transferência de fundos
        boolean debitSuccess = query.movement(sourceAccount,"Debit",amount , "Transfer");
        boolean creditSuccess = query.movement(targetAccount,"Credit",amount , "Transfer");

        return debitSuccess && creditSuccess; // Transferência bem-sucedida se ambas as operações forem bem-sucedidas
    }

    /**
     * Envia um e-mail para o destinatário da transferência.
     *
     * @param recipientEmail Endereço de e-mail do destinatário.
     * @param subject        Assunto do e-mail.
     * @param text           Corpo do e-mail.
     */
    private void sendEmail(String recipientEmail, String subject, String text) {
        final String username = "projetoptda@gmail.com";
        final String password = "gcue jaff wcib cklg";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

            System.out.println("Email enviado com sucesso!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Alterna para a tela de menu principal.
     *
     * @param event O evento associado à ação.
     * @throws IOException Exceção de entrada/saída.
     */
    public void switchToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = loader.load();
        ControllerMenu menuController = loader.getController();
        String clientName = query.getClientName(sourceAccountNumber);
        menuController.setClientName(clientName);
        menuController.setClientAccountNumber(sourceAccountNumber);
        Stage stage = (Stage) buttonGoBack.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Valida a entrada do user para o número da conta de destino e o valor da transferência.
     *
     * @param targetAccount Número da conta de destino.
     * @param amount     Valor da transferência.
     * @return True se a entrada for válida, False caso contrário.
     */
    protected boolean validateInput(String targetAccount, String amount) {
        // Valida se a conta de destino existe e o valor é um número float válido
        if (!targetAccount.matches("^\\d{20}$")) {
            return false; // O número da conta de destino deve ser um número de 20 dígitos
        }

        if (!amount.matches("^\\d+(\\.\\d+)?$")) {
            return false; // O valor deve ser um número float válido
        }
        return true;
    }

    /**
     * Exibe uma mensagem de erro usando um Alert.
     *
     * @param message Mensagem de erro a ser exibida.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Mostra o Alert
        alert.showAndWait();
    }

    /**
     * Aplica o estilo de validação com borda vermelha.
     */
    private void applyValidationStyle() {
        labelValidation.setTextFill(Color.RED);
        Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
        transferAmount.setBorder(border);
    }

    /**
     * Limpa os estilos de validação.
     */
    private void clearValidationStyles() {
        labelValidation.setText("");
        transferAmount.setBorder(null);
    }
}