package atm.ptda_atm;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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

    Random random =  new Random();
    StringBuilder movementID = new StringBuilder();
    private String sourceCardNumber;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private PreparedStatement preparedStatement2;
    private PreparedStatement preparedStatement3;
    private PreparedStatement preparedStatement4;
    private ResultSet rs;
    private ResultSet rsName;
    private ResultSet rsBalance;
    private ResultSet rsEmail;

    public void setClientCardNumber(String sourceCardNumber) {
        this.sourceCardNumber = sourceCardNumber;
        initialize(connection);
    }

    public void initialize(Connection connection) {
        targetCardNumber.setOnKeyTyped(event -> clearValidationStyles());
        transferAmount.setOnKeyTyped(event -> clearValidationStyles());

        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(Cursor.DEFAULT));

        buttonTransfer.setOnMouseEntered(e -> buttonTransfer.setCursor(Cursor.HAND));
        buttonTransfer.setOnMouseExited(e -> buttonTransfer.setCursor(Cursor.DEFAULT));

        this.connection = connection;
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
            float availableBalance = getAvailableBalance(sourceCardNumber);
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
                    boolean success = performFundTransfer(sourceCardNumber, targetCard, transferAmount);

                    if (success) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();

                        labelValidation.setText(amount + "€ has been withdrawn from your account!");
                        labelValidation.setTextFill(Color.GREEN);

                        try {
                            movement(sourceCardNumber,formatter.format(now),"Debit", Float.parseFloat(amount),"Transfer");
                        } catch (SQLException ex) {
                            showError("Error saving the movement!");
                        }

                        String recipientEmail = getClientEmail(sourceCardNumber);
                        String subject = "Transfer";
                        String message = "Subject: Transfer Notification\n" +
                                "Dear "+getClientName(sourceCardNumber)+",\n" +
                                "We are pleased to inform you that a transfer of "+ amount +"€ has been successfully made from your account. This transfer was processed on "+ formatter.format(now) +".\n" +
                                "Should you have any questions or need further clarification, please do not hesitate to reach out to us. We are here to assist you.\n" +
                                "Best regards,\n" +
                                "ByteBank";
                        sendEmail(recipientEmail, subject, message);

                        try {
                            movement(targetCard,formatter.format(now),"Credit", Float.parseFloat(amount),"Transfer");
                        } catch (SQLException ex) {
                            showError("Error saving the movement!");
                        }

                        String recipientEmailTarget = getClientEmail(targetCard);
                        String subjectTarget = "Transfer";

                        String messageTarge = "Subject: Transfer Notification\n"+
                                "Dear "+getClientName(targetCard)+",\n" +
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

    private void updateAccountBalance(String clientCardNumber, float amount) throws SQLException {
        String updateQuery = "UPDATE BankAccount SET accountBalance = accountBalance + ? WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setFloat(1, amount);
            preparedStatement.setString(2, clientCardNumber);
            preparedStatement.executeUpdate();
        }
    }

    private boolean performFundTransfer(String sourceCard, String targetCard, float amount) {
        try {
            // Check if the source card has sufficient balance
            float sourceBalance = getAvailableBalance(sourceCard);
            if (sourceBalance < amount) {
                return false; // Insufficient balance
            }

            // Perform the fund transfer logic
            updateAccountBalance(sourceCard, -amount); // Deduct from the source account
            updateAccountBalance(targetCard, amount);  // Add to the target account

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean movement(String clientCardNumber, String date, String type, float value, String description) throws SQLException {
        //ID do movimento
        for (int i = 0; i < 5; i++) {
            int digito = random.nextInt(10);
            movementID.append(digito);
        }

        preparedStatement3 = connection.prepareStatement("INSERT INTO Movement VALUES (?,?,?,?,?,?)");
        preparedStatement3.setString(1, String.valueOf(movementID));
        preparedStatement3.setString(2, clientCardNumber);
        preparedStatement3.setString(3, date);
        preparedStatement3.setString(4, type);
        preparedStatement3.setFloat(5, value);
        preparedStatement3.setString(6, description);

        ResultSet rs = preparedStatement3.executeQuery();

        if(rs.next()) {
            return true;
        }
        else {
            return false;
        }
    }

    public String getClientName(String clientCardNumber) {
        try {
            String query = "SELECT clientName FROM BankAccount WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber = ?)";
            preparedStatement2 = connection.prepareStatement(query);
            preparedStatement2.setString(1, clientCardNumber);
            rsName = preparedStatement2.executeQuery();

            if (rsName.next()) {
                return rsName.getString("clientName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsName != null) {
                    rsName.close();
                }
                if (preparedStatement2 != null) {
                    preparedStatement2.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;  // Retorna null se não conseguir obter o clientName
    }

    // Method to get the available balance in the account
    private float getAvailableBalance(String clientCardNumber) {
        try {
            String query = "SELECT accountBalance FROM BankAccount WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber  = ?)";
            preparedStatement3 = connection.prepareStatement(query);
            preparedStatement3.setString(1, clientCardNumber);
            rs = preparedStatement3.executeQuery();

            if (rs.next()) {
                return rs.getFloat("accountBalance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement3 != null) {
                    preparedStatement3.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return 0.0f;  // Return 0.0 in case of an error
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

    // Método para obter o email do cliente
    private String getClientEmail(String clientCardNumber) {
        try {
            String query = "SELECT email FROM BankAccount WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber = ?)";
            preparedStatement4 = connection.prepareStatement(query);
            preparedStatement4.setString(1, clientCardNumber);
            rsEmail = preparedStatement4.executeQuery();

            if (rsEmail.next()) {
                return rsEmail.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsEmail != null) {
                    rsEmail.close();
                }
                if (preparedStatement4 != null) {
                    preparedStatement4.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;  // Retorna null se não conseguir obter o email
    }

    public void switchToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = loader.load();
        ControllerMenu menuController = loader.getController();
        String clientName = getClientName(sourceCardNumber);
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
