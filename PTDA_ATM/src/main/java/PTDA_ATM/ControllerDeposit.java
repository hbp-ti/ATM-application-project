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
import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Controlador para a funcionalidade de depósito.
 */
public class ControllerDeposit {

    /**
     * Botão para realizar o depósito.
     */
    @FXML
    private Button buttonDeposit;

    /**
     * Barra de progresso para exibir o progresso do depósito.
     */
    @FXML
    private ProgressBar progressDeposit;

    /**
     * Campo de texto para inserir o valor do depósito.
     */
    @FXML
    private TextField amount;

    /**
     * Botão para voltar ao menu principal.
     */
    @FXML
    private Button buttonGoBack;

    /**
     * Rótulo para exibir mensagens de validação ou sucesso.
     */
    @FXML
    private Label labelValidacao;

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
     * Flag para indicar se o depósito foi bem-sucedido.
     */
    private boolean success;

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
     * Inicializa o controlador.
     */
    public void initialize() {
        amount.setOnKeyTyped(event -> clearValidationStyles());

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

        buttonDeposit.setOnMouseEntered(e -> {
            buttonDeposit.setCursor(Cursor.HAND);
            buttonDeposit.setTranslateY(2); // Altere o valor conforme necessário
            buttonDeposit.setEffect(reflection); // Adiciona reflexo ao passar o mouse
        });
        buttonDeposit.setOnMouseExited(e -> {
            buttonDeposit.setCursor(Cursor.DEFAULT);
            buttonDeposit.setTranslateY(0); // Retorna à posição original
            buttonDeposit.setEffect(null); // Remove o efeito de sombra ao passar o mouse
        });
    }

    /**
     * Realiza a lógica de depósito.
     *
     * @param event O evento associado à ação.
     * @throws IOException Exceção de entrada/saída.
     */
    public void deposit(ActionEvent event) throws IOException {
        if (!validateInput(amount.getText())) {
            labelValidacao.setText("Invalid amount");
            applyValidationStyle();
        } else {
            float depositAmount = Float.parseFloat(amount.getText());

            if (depositAmount > 10000) {
                labelValidacao.setText("Deposit amount exceeds the limit of 10000€");
                applyValidationStyle();
            } else {
                progressDeposit.setProgress(0.0);
                Duration duration = Duration.seconds(3);
                KeyFrame keyFrame = new KeyFrame(duration, new KeyValue(progressDeposit.progressProperty(), 1.0));
                Timeline timeline = new Timeline(keyFrame);
                timeline.setCycleCount(1);
                timeline.play();

                timeline.setOnFinished(e -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();

                    try {
                        query.movement(clientAccountNumber, "Credit", depositAmount, "Deposit");
                        labelValidacao.setText(String.format("%.2f€ has been credited to your account!", depositAmount));
                        labelValidacao.setTextFill(Color.GREEN);

                        String recipientEmail = query.getClientEmail(clientAccountNumber);
                        String subject = "Deposit";
                        String message = "Subject: Deposit Notification\n" +
                                "Dear " + query.getClientName(clientAccountNumber) + ",\n" +
                                "We are pleased to inform you that a deposit of " + String.format("%.2f€", depositAmount) +
                                " has been successfully credited to your account. This deposit was processed on " +
                                formatter.format(now) + " and is now available for your use.\n" +
                                "Should you have any questions or need further clarification, please do not hesitate to reach out to us. We are here to assist you.\n" +
                                "Best regards,\n" +
                                "ByteBank";
                        sendEmail(recipientEmail, subject, message);

                        PauseTransition pause = new PauseTransition(Duration.seconds(3));
                        pause.setOnFinished(events -> {
                            try {
                                switchToMenu(event);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                        pause.play();
                    } catch (SQLException ex) {
                        showError("Error saving the movement!");
                    }
                });
            }
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
        String clientName = query.getClientName(clientAccountNumber);
        menuController.setClientName(clientName);
        menuController.setClientAccountNumber(clientAccountNumber);
        Stage stage = (Stage) buttonGoBack.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Valida a entrada do usuário para o valor do depósito.
     *
     * @param depositAmount Valor do depósito a ser validado.
     * @return True se a entrada for válida, False caso contrário.
     */
    protected boolean validateInput(String depositAmount) {
        // Verifica se o valor do depósito é um número float válido
        if (!depositAmount.matches("^\\d+(\\.\\d+)?$")) {
            return false; // Não é um número float válido
        }
        return true;
    }

    /**
     * Envia um email de notificação para o cliente após o depósito.
     *
     * @param recipientEmail Endereço de email do destinatário.
     * @param subject        Assunto do email.
     * @param text           Corpo do email.
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
     * Exibe uma mensagem de erro usando um Alert.
     *
     * @param message Mensagem de erro a ser exibida.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Mostrar o Alert
        alert.showAndWait();
    }

    /**
     * Aplica o estilo de validação com borda vermelha.
     */
    private void applyValidationStyle() {
        labelValidacao.setTextFill(Color.RED);
        Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
        amount.setBorder(border);
    }

    /**
     * Limpa os estilos de validação.
     */
    private void clearValidationStyles() {
        labelValidacao.setText("");
        amount.setBorder(null);
    }
}
