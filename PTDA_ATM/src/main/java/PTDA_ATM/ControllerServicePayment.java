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
import java.util.HashMap;
import java.util.Properties;

public class ControllerServicePayment {

    @FXML
    private TextField entity;

    @FXML
    private TextField reference;

    @FXML
    private TextField amount;

    @FXML
    private Button buttonGoBack;

    @FXML
    private Label labelValidation;

    @FXML
    private ProgressBar progressService;

    @FXML
    private Button buttonPay;

    private String clientCardNumber;
    Query query = new Query();

    public void initialize() {
        entity.setOnKeyTyped(event -> clearValidationStyles());
        reference.setOnKeyTyped(event -> clearValidationStyles());
        amount.setOnKeyTyped(event -> clearValidationStyles());

        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(Cursor.DEFAULT));

        buttonPay.setOnMouseEntered(e -> buttonPay.setCursor(Cursor.HAND));
        buttonPay.setOnMouseExited(e -> buttonPay.setCursor(Cursor.DEFAULT));

    }

    public void setClientCardNumber(String clientCardNumber) {
        this.clientCardNumber = clientCardNumber;
        initialize();
    }

    public void payService(ActionEvent event) {
        String ent = entity.getText();
        String ref = reference.getText();
        String am = amount.getText();

        if (!validateInput(ent,ref,am)) {
            labelValidation.setText("Invalid input. Check and try again.");
            applyValidationStyle();
        } else {
            float payAmount = Float.parseFloat(am);

            // Check if the transfer amount is greater than the available balance
            float availableBalance = query.getAvailableBalance(clientCardNumber);
            if (payAmount > availableBalance) {
                labelValidation.setText("Insufficient funds");
                applyValidationStyle();
            } else {
                progressService.setProgress(0.0);
                Duration duration = Duration.seconds(3);
                KeyFrame keyFrame = new KeyFrame(duration, new KeyValue(progressService.progressProperty(), 1.0));
                Timeline timeline = new Timeline(keyFrame);
                timeline.setCycleCount(1);
                timeline.play();
                timeline.setOnFinished(e -> {
                    if (!validatePayment(ent, ref, am)) {
                        labelValidation.setText("Payment details wrong. Check and try again.");
                        applyValidationStyle();
                    } else {
                        boolean success = false;
                        try {
                            success = performServicePayment(clientCardNumber, Float.parseFloat(am));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }

                        if (success) {

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();

                            labelValidation.setText("Bill " + ref + " payment was successful!");
                            labelValidation.setTextFill(Color.GREEN);

                            String recipientEmail = query.getClientEmail(clientCardNumber);
                            String subject = "Bill Payment";
                            String message = "Subject: Bill Payment Notification\n" +
                                    "Dear " + query.getClientName(clientCardNumber) + ",\n" +
                                    "Entity: " + ent + "\n" +
                                    "Reference: " + ref + "\n" +
                                    "Amount: " + am + "€\n" +
                                    "has been successfully made from your account. This payment was processed on " + formatter.format(now) + ".\n" +
                                    "Should you have any questions or need further clarification, please do not hesitate to reach out to us. We are here to assist you.\n" +
                                    "Best regards,\n" +
                                    "ByteBank";
                            sendEmail(recipientEmail, subject, message);

                            PauseTransition pauseValidation = new PauseTransition(Duration.seconds(3));
                            pauseValidation.setOnFinished(events -> {
                                try {
                                    switchToMenu(event);
                                } catch (IOException es) {
                                    es.printStackTrace(); // Trate adequadamente o erro na transição para o menu
                                }
                            });
                            pauseValidation.play();
                        } else {
                            showError("Service payment unsuccessful. Check the details and try again.");
                        }
                    }
                });
            }
        }
    }

    public void switchToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuPayment.fxml"));
        Parent root = loader.load();
        ControllerMenuPayment Controller = loader.getController();
        String clientName = query.getClientName(clientCardNumber);
        Controller.setClientName(clientName);
        Controller.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonGoBack.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private boolean performServicePayment(String clientCardNumber, float amount) throws SQLException {
        // Check if the source card has sufficient balance
        float Balance = query.getAvailableBalance(clientCardNumber);
        if (Balance < amount) {
            return false; // Insufficient balance
        }

        // Perform the fund transfer logic
        boolean debitSuccess = query.movement(clientCardNumber, "Debit", amount,"Service Payment");

        return debitSuccess;
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

    private boolean validateInput(String entity, String reference, String amount) {
        if (!entity.matches("^\\d{5}$")) {
            return false; // Entity should have 5 numeric digits
        }

        if (!reference.matches("^\\d{9}$")) {
            return false; // Reference should have 9 numeric digits
        }

        if (!amount.matches("^\\d+(\\.\\d+)?$")) {
            return false; // Amount should be a valid float
        }

        return true;
    }

    private HashMap<String, Object> getHashMap() {
        Bills bills = new Bills();
        return bills.getPayment();
    }

    private boolean validatePayment(String entity, String reference, String amount) {
        HashMap<String, Object> bill = getHashMap();
        boolean isEntityValid = false;
        boolean isValueValid = false;
        boolean isReferenceValid = false;

        isReferenceValid = bill.containsKey(reference);

        for (Object payment : bill.values()) {
            if (payment instanceof Services) {
                Services service = (Services) payment;
                if (entity.equals(service.getEntity())) {
                    isEntityValid = true; // Encontrou a entidade correspondente nos serviços
                }
                if (Double.parseDouble(amount) == service.getValue()) {
                    isValueValid = true; // Encontrou o valor correspondente nos serviços
                }
            }
        }

        return isReferenceValid && isEntityValid && isValueValid;
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
        entity.setBorder(border);
        reference.setBorder(border);
        amount.setBorder(border);
    }

    // Método para limpar os estilos de validação
    private void clearValidationStyles() {
        labelValidation.setText("");
        entity.setBorder(null);
        reference.setBorder(null);
        amount.setBorder(null);
    }
}
