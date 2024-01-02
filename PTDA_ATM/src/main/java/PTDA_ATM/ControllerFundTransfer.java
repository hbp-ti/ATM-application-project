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
     * Campo de texto para o número do cartão de destino da transferência.
     */
    @FXML
    private TextField targetCardNumber;

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
     * Número do cartão de origem da transferência.
     */
    private String sourceCardNumber;

    /**
     * Objeto para executar consultas no banco de dados.
     */
    private final Query query = new Query();

    /**
     * Define o número do cartão do cliente de origem.
     *
     * @param sourceCardNumber Número do cartão do cliente de origem.
     */
    public void setClientCardNumber(String sourceCardNumber) {
        this.sourceCardNumber = sourceCardNumber;
        initialize();
    }

    /**
     * Inicializa o controlador.
     */
    public void initialize() {
        targetCardNumber.setOnKeyTyped(event -> clearValidationStyles());
        transferAmount.setOnKeyTyped(event -> clearValidationStyles());

        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(Cursor.DEFAULT));

        buttonTransfer.setOnMouseEntered(e -> buttonTransfer.setCursor(Cursor.HAND));
        buttonTransfer.setOnMouseExited(e -> buttonTransfer.setCursor(Cursor.DEFAULT));
    }

    /**
     * Realiza a lógica de transferência de fundos.
     *
     * @param event O evento associado à ação.
     * @throws IOException Exceção de entrada/saída.
     */
    public void transfer(ActionEvent event) throws IOException {
        String targetCard = targetCardNumber.getText();
        String amount = transferAmount.getText();

        if (!validateInput(targetCard, amount)) {
            labelValidation.setText("Invalid input. Check and try again.");
            applyValidationStyle();
        } else {
            float transferAmount = Float.parseFloat(amount);

            // Verifica se o valor da transferência é maior que o saldo disponível
            float availableBalance = query.getAvailableBalance(sourceCardNumber);
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
                        success = performFundTransfer(sourceCardNumber, targetCard, transferAmount);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                    if (success) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();

                        labelValidation.setText(amount + "€ has been withdrawn from your account!");
                        labelValidation.setTextFill(Color.GREEN);

                        String recipientEmail = query.getClientEmail(sourceCardNumber);
                        String subject = "Transfer";
                        String message = "Subject: Transfer Notification\n" +
                                "Dear "+query.getClientName(sourceCardNumber)+",\n" +
                                "We are pleased to inform you that a transfer of "+ amount +"€ has been successfully made from your account. This transfer was processed on "+ formatter.format(now) +".\n" +
                                "Should you have any questions or need further clarification, please do not hesitate to reach out to us. We are here to assist you.\n" +
                                "Best regards,\n" +
                                "ByteBank";
                        sendEmail(recipientEmail, subject, message);

                        // Não é necessário chamar movement novamente aqui

                        String recipientEmailTarget = query.getClientEmail(targetCard);
                        String subjectTarget = "Transfer";

                        String messageTarget = "Subject: Transfer Notification\n"+
                                "Dear "+query.getClientName(targetCard)+",\n" +
                                "We are pleased to inform you that a transfer of "+ amount +"€ has been successfully made to your account. This transfer was processed on "+ formatter.format(now) +".\n" +
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
                    } else {
                        showError("Transfer unsuccessful. Check the target card number and try again.");
                    }
                });
            }
        }
    }

    /**
     * Executa a lógica de transferência de fundos no banco de dados.
     *
     * @param sourceCard   Número do cartão de origem.
     * @param targetCard   Número do cartão de destino.
     * @param amount       Valor da transferência.
     * @return True se a transferência for bem-sucedida, False caso contrário.
     * @throws SQLException Exceção de SQL.
     */
    private boolean performFundTransfer(String sourceCard, String targetCard, float amount) throws SQLException {
        // Verifica se o cartão de origem tem saldo suficiente
        float sourceBalance = query.getAvailableBalance(sourceCard);
        if (sourceBalance < amount) {
            return false; // Saldo insuficiente
        }

        // Executa a lógica de transferência de fundos
        boolean debitSuccess = query.movement(sourceCard,"Debit",amount , "Transfer");
        boolean creditSuccess = query.movement(targetCard,"Credit",amount , "Transfer");

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
        String clientName = query.getClientName(sourceCardNumber);
        menuController.setClientName(clientName);
        menuController.setClientCardNumber(sourceCardNumber);
        Stage stage = (Stage) buttonGoBack.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Valida a entrada do user para o número do cartão de destino e o valor da transferência.
     *
     * @param targetCard Número do cartão de destino.
     * @param amount     Valor da transferência.
     * @return True se a entrada for válida, False caso contrário.
     */
    private boolean validateInput(String targetCard, String amount) {
        // Valida se o cartão de destino existe e o valor é um número float válido
        if (!targetCard.matches("^\\d{10}$")) {
            return false; // O número do cartão de destino deve ser um número de 16 dígitos
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