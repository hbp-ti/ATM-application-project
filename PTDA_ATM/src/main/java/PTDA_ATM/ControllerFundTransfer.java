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

public class ControllerFundTransfer {
    @FXML
    private TextField targetCardNumber;

    @FXML
    private TextField transferAmount;

    @FXML
    private ProgressBar progressTransfer;

    @FXML
    private Button buttonTransfer;

    @FXML
    private Button buttonGoBack;

    @FXML
    private Label labelValidation;

    private String sourceCardNumber;
    Query query = new Query();


    public void setClientCardNumber(String sourceCardNumber) {
        this.sourceCardNumber = sourceCardNumber;
        initialize();
    }

    public void initialize() {
        targetCardNumber.setOnKeyTyped(event -> clearValidationStyles());
        transferAmount.setOnKeyTyped(event -> clearValidationStyles());

        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(Cursor.DEFAULT));

        buttonTransfer.setOnMouseEntered(e -> buttonTransfer.setCursor(Cursor.HAND));
        buttonTransfer.setOnMouseExited(e -> buttonTransfer.setCursor(Cursor.DEFAULT));
    }

    public void transfer(ActionEvent event) throws IOException {
        String targetCard = targetCardNumber.getText();
        String amount = transferAmount.getText();

        if (!validateInput(targetCard, amount)) {
            labelValidation.setText("Invalid input. Check and try again.");
            applyValidationStyle();
        } else {
            float transferAmount = Float.parseFloat(amount);

            // Check if the transfer amount is greater than the available balance
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

                        String messageTarge = "Subject: Transfer Notification\n"+
                                "Dear "+query.getClientName(targetCard)+",\n" +
                                "We are pleased to inform you that a transfer of "+ amount +"€ has been successfully made to your account. This transfer was processed on "+ formatter.format(now) +".\n" +
                                "Should you have any questions or need further clarification, please do not hesitate to reach out to us. We are here to assist you.\n" +
                                "Best regards,\n" +
                                "ByteBank";
                        sendEmail(recipientEmailTarget, subjectTarget, messageTarge);

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

    private boolean performFundTransfer(String sourceCard, String targetCard, float amount) throws SQLException {
        // Check if the source card has sufficient balance
        float sourceBalance = query.getAvailableBalance(sourceCard);
        if (sourceBalance < amount) {
            return false; // Insufficient balance
        }

        // Perform the fund transfer logic
        boolean debitSuccess = query.movement(sourceCard,"Debit",amount , "Transfer");
        boolean creditSuccess = query.movement(targetCard,"Credit",amount , "Transfer");

        return debitSuccess && creditSuccess; // Transfer successful if both operations are successful
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

    private boolean validateInput(String targetCard, String amount) {
        // Validate if the target card exists and the amount is a valid float
        if (!targetCard.matches("^\\d{10}$")) {
            return false; // Target card number should be a 16-digit number
        }

        if (!amount.matches("^\\d+(\\.\\d+)?$")) {
            return false; // Amount should be a valid float
        }
        return true;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Mostrar o Alert
        alert.showAndWait();
    }

    // Método para aplicar o estilo de borda vermelho
    private void applyValidationStyle() {
        labelValidation.setTextFill(Color.RED);
        Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
        transferAmount.setBorder(border);
    }

    // Método para limpar os estilos de validação
    private void clearValidationStyles() {
        labelValidation.setText("");
        transferAmount.setBorder(null);
    }
}
