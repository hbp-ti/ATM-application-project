package atm.ptda_atm;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Random;

public class ControllerChargePhone {

    @FXML
    private Button buttonCharge;

    @FXML
    private Button buttonGoBack;

    @FXML
    private Label labelValidacao;

    @FXML
    private TextField amount;

    @FXML
    private TextField phoneNumber;

    Random random =  new Random();
    StringBuilder movementID = new StringBuilder();
    private String clientCardNumber;
    private PreparedStatement preparedStatement;
    private PreparedStatement preparedStatement2;
    private PreparedStatement preparedStatement3;
    private ResultSet rs;
    private ResultSet rsEmailName;
    private ResultSet rsName;
    private Connection connection;


    public void initialize(Connection connection) {
        phoneNumber.setOnKeyTyped(event -> clearValidationStyles());
        amount.setOnKeyTyped(event -> clearValidationStyles());

        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(Cursor.DEFAULT));

        buttonCharge.setOnMouseEntered(e -> buttonCharge.setCursor(Cursor.HAND));
        buttonCharge.setOnMouseExited(e -> buttonCharge.setCursor(Cursor.DEFAULT));

        this.connection = connection;
    }

    public void setClientCardNumber(String clientCardNumber) {
        this.clientCardNumber = clientCardNumber;
        initialize(connection);
    }

    public void chargePhone(ActionEvent event) throws IOException, SQLException {
        String phoneNumberr = phoneNumber.getText();
        String amountt = amount.getText();


        if (!validateInput(phoneNumberr, amountt)) {
            labelValidacao.setText("Invalid input. Check and try again.");
            applyValidationStyle();
        } else {
            float chargeAmount = Float.parseFloat(amountt);

            float availableBalance = getAvailableBalance(clientCardNumber);

            if (chargeAmount > availableBalance) {
                labelValidacao.setText("Insufficient funds");
                applyValidationStyle();
            } else {
                boolean success = performPhoneCharge(clientCardNumber, phoneNumberr, chargeAmount);

                if (success) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();

                    labelValidacao.setText(amountt + "€ has been charged to "+phoneNumberr+"!");
                    labelValidacao.setTextFill(Color.GREEN);

                    try {
                        movement(clientCardNumber,formatter.format(now),"Debit", Float.parseFloat(amount.getText()),"Phone Charge");
                    } catch (SQLException ex) {
                        showError("Error saving the movement!");
                    }

                    String recipientEmail = getClientEmail(clientCardNumber);
                    String subject = "Transfer";
                    String message = "Subject: Phone Credit Notification\n" +
                            "Dear " + getClientName(clientCardNumber) + ",\n" +
                            "We are pleased to inform you that a credit of " + amountt + "€ has been successfully loaded onto the phone: "+phoneNumberr+". This credit was processed on " + formatter.format(now) + ".\n" +
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
                    showError("Phone Charge unsuccessful. Check the phone number and try again.");
                }
            }

        }
    }

    private boolean doesPhoneNumberExist(String phoneNumber) throws SQLException {
        String query = "SELECT phoneNumber FROM BankAccount WHERE phoneNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, phoneNumber);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return true;
            }
        }
        return false;
    }

    private boolean performPhoneCharge(String clientCardNumber, String phoneNumber, float amount) throws SQLException {
        if (doesPhoneNumberExist(phoneNumber)) {
            try {
                String query = "UPDATE BankAccount SET accountBalance = accountBalance - ? WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber  = ?)";
                preparedStatement3 = connection.prepareStatement(query);
                preparedStatement3.setFloat(1, amount);
                preparedStatement3.setString(2, clientCardNumber);
                int linhasAftedas = preparedStatement3.executeUpdate();

                if (linhasAftedas > 0) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
        return false;
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

    public void switchToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = loader.load();
        ControllerMenu menuController = loader.getController();
        String clientName = getClientName(clientCardNumber);
        menuController.setClientName(clientName);
        menuController.setClientCardNumber(clientCardNumber);
        Stage stage = (Stage) buttonGoBack.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private boolean validateInput(String phoneNumber, String amount) {
        // Validate if the phone number exists and the amount is a valid float
        if (!phoneNumber.matches("^\\d{1,9}$")) {
            return false; // Phone number should have up to 9 digits
        }

        if (!amount.matches("^\\d+(\\.\\d+)?$")) {
            return false; // Amount should be a valid float
        }
        return true;
    }

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

    public String getClientName(String clientCardNumber) {
        try {
            String query = "SELECT clientName FROM BankAccount WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber = ?)";
            preparedStatement3 = connection.prepareStatement(query);
            preparedStatement3.setString(1, clientCardNumber);
            rsName = preparedStatement3.executeQuery();

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
                if (preparedStatement3 != null) {
                    preparedStatement3.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;  // Retorna null se não conseguir obter o clientName
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

    private String getClientEmail(String clientCardNumber) {
        try {
            String query = "SELECT email FROM BankAccount WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber = ?)";
            preparedStatement3 = connection.prepareStatement(query);
            preparedStatement3.setString(1, clientCardNumber);
            rsEmailName = preparedStatement3.executeQuery();

            if (rsEmailName.next()) {
                return rsEmailName.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsEmailName != null) {
                    rsEmailName.close();
                }
                if (preparedStatement3 != null) {
                    preparedStatement3.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;  // Retorna null se não conseguir obter o email
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
        phoneNumber.setBorder(border);
        amount.setBorder(border);
    }

    // Método para limpar os estilos de validação
    private void clearValidationStyles() {
        labelValidacao.setText("");
        phoneNumber.setBorder(null);
        amount.setBorder(null);
    }
}
