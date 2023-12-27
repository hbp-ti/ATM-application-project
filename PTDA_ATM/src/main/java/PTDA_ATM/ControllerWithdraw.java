package PTDA_ATM;

import SQL.Query;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.util.Duration;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class ControllerWithdraw {
    @FXML
    private Button buttonWithdraw;

    @FXML
    private ProgressBar progressWithdraw;
    @FXML
    private TextField amount;
    @FXML
    private Button buttonGoBack;
    @FXML
    private Label labelValidacao;

    private String clientCardNumber;
    private Query query = new Query();

    public void setClientCardNumber(String clientCardNumber) {
        this.clientCardNumber = clientCardNumber;
        initialize();
    }

    public void initialize() {
        amount.setOnKeyTyped(event -> clearValidationStyles());

        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(Cursor.DEFAULT));

        buttonWithdraw.setOnMouseEntered(e -> buttonWithdraw.setCursor(Cursor.HAND));
        buttonWithdraw.setOnMouseExited(e -> buttonWithdraw.setCursor(Cursor.DEFAULT));
    }

    public void withdraw(ActionEvent event) throws IOException {
        if (!validateInput(amount.getText())) {
            labelValidacao.setText("Invalid amount");
            applyValidationStyle();
        } else {
            float withdrawalAmount = Float.parseFloat(amount.getText());
            float availableBalance = query.getAvailableBalance(clientCardNumber);

            if (withdrawalAmount > availableBalance) {
                labelValidacao.setText("Insufficient funds");
                applyValidationStyle();
            } else {
                progressWithdraw.setProgress(0.0);
                Duration duration = Duration.seconds(3);
                KeyFrame keyFrame = new KeyFrame(duration, new KeyValue(progressWithdraw.progressProperty(), 1.0));
                Timeline timeline = new Timeline(keyFrame);
                timeline.setCycleCount(1);
                timeline.play();

                timeline.setOnFinished(e -> {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();

                        float remainingBalance = availableBalance - withdrawalAmount;

                        if (query.movement(clientCardNumber, "Debit", withdrawalAmount, "Withdraw")) {
                            labelValidacao.setText(String.format("%.2f€ has been withdrawn from your account!", withdrawalAmount));
                            labelValidacao.setTextFill(Color.GREEN);

                            String recipientEmail = query.getClientEmail(clientCardNumber);
                            String subject = "Withdraw";
                            String message = "Subject: Withdraw Notification\n" +
                                    "Dear " + query.getClientName(clientCardNumber) + ",\n" +
                                    "We are pleased to inform you that a withdraw of " + String.format("%.2f€", withdrawalAmount) +
                                    " has been successfully withdrawn from your account. This withdraw was processed on " +
                                    formatter.format(now) + ".\n" +
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
                        } else {
                            Platform.runLater(() -> showError("Withdraw unsuccessful!"));
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        Platform.runLater(() -> showError("Error processing the withdrawal!"));
                    }
                });
            }
        }
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


    private boolean validateInput(String withdrawAmount) {
        // Verifica se o valor do depósito é um número float válido
        if (!withdrawAmount.matches("^\\d+(\\.\\d+)?$")) {
            return false;
        }
        return true;
    }

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

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            // Mostrar o Alert
            alert.showAndWait();
        });
    }

    // Método para aplicar o estilo de borda vermelho
    private void applyValidationStyle() {
        labelValidacao.setTextFill(Color.RED);
        Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
        amount.setBorder(border);
    }

    // Método para limpar os estilos de validação
    private void clearValidationStyles() {
        labelValidacao.setText("");
        amount.setBorder(null);
    }
}
