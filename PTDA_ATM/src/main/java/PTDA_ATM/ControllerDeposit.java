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
import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class ControllerDeposit {
    @FXML
    private Button buttonDeposit;
    @FXML
    private ProgressBar progressDeposit;
    @FXML
    private TextField amount;
    @FXML
    private Button buttonGoBack;
    @FXML
    private Label labelValidacao;


    private String clientCardNumber;
    Query query = new Query();

    private boolean success;

    public void setClientCardNumber(String clientCardNumber) {
        this.clientCardNumber = clientCardNumber;
        initialize();
    }

    public void initialize() {
        amount.setOnKeyTyped(event -> clearValidationStyles());

        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(Cursor.DEFAULT));

        buttonDeposit.setOnMouseEntered(e -> buttonDeposit.setCursor(Cursor.HAND));
        buttonDeposit.setOnMouseExited(e -> buttonDeposit.setCursor(Cursor.DEFAULT));
    }

    public void deposit(ActionEvent event) throws IOException {
        if (!validateInput(amount.getText())) {
            labelValidacao.setText("Invalid amount");
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

                float depositAmount = Float.parseFloat(amount.getText());

                try {
                    query.movement(clientCardNumber, "Credit", depositAmount, "Deposit");
                    labelValidacao.setText(String.format("%.2f€ has been credited to your account!", depositAmount));
                    labelValidacao.setTextFill(Color.GREEN);

                    String recipientEmail = query.getClientEmail(clientCardNumber);
                    String subject = "Deposit";
                    String message = "Subject: Deposit Notification\n" +
                            "Dear " + query.getClientName(clientCardNumber) + ",\n" +
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

    private boolean validateInput(String depositAmount) {
        // Verifica se o valor do depósito é um número float válido
        if (!depositAmount.matches("^\\d+(\\.\\d+)?$")) {
            return false; // Não é um número float válido
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Mostrar o Alert
        alert.showAndWait();
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
