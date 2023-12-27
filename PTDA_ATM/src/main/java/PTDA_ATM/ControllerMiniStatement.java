package PTDA_ATM;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

public class ControllerMiniStatement {

    @FXML
    private Button buttonGoBack;

    @FXML
    private Button buttonEmail;

    @FXML
    private Label miniStatementLabel;
    StringBuilder miniStatement = new StringBuilder();
    private String clientCardNumber;
    private PreparedStatement preparedStatement;
    private PreparedStatement preparedStatement4;
    private PreparedStatement preparedStatement3;
    private ResultSet rs;
    private ResultSet rsEmail;
    private ResultSet rsName;
    private Connection connection;


    public void initialize(Connection connection) {

        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(Cursor.DEFAULT));

        buttonEmail.setOnMouseEntered(e -> buttonEmail.setCursor(Cursor.HAND));
        buttonEmail.setOnMouseExited(e -> buttonEmail.setCursor(Cursor.DEFAULT));

        this.connection = connection;
    }

    public void setClientCardNumber(String clientCardNumber) {
        this.clientCardNumber = clientCardNumber;
        initialize(connection);
        loadMiniStatement();
    }

    private void loadMiniStatement() {
        try {
            String query = "SELECT * FROM Movement WHERE cardNumber = ? ORDER BY movementDate DESC LIMIT 15";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientCardNumber);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String movementDescription = rs.getString("movementDescription");
                String movementDate = rs.getString("movementDate");
                String movementValue = rs.getString("movementValue");

                miniStatement.append(movementDate).append(" - ").append(movementDescription)
                        .append(": ").append(movementValue).append("€\n");
            }

            miniStatementLabel.setText(miniStatement.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public void email(ActionEvent event) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String recipientEmail = getClientEmail(clientCardNumber);
        String subject = "Mini Statement";
        String message = "Subject: Account Statement\n" +
                "Dear " + getClientName(clientCardNumber) + ",\n" +
                "We are pleased to provide your account statement as of " + formatter.format(now) + ":\n\n" +
                miniStatement.toString() + "\n" +
                "Should you have any questions or need further clarification, please do not hesitate to reach out to us. We are here to assist you.\n" +
                "Best regards,\n" +
                "ByteBank";
        sendEmail(recipientEmail, subject, message);

        showSuccessPopup("Email sent successfully!");
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

    private void showSuccessPopup(String message) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Success");

        Label successLabel = new Label(message);
        successLabel.setWrapText(true);
        successLabel.setMaxWidth(Double.MAX_VALUE);
        successLabel.setAlignment(Pos.CENTER);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            // Remove o efeito de desfoque da janela principal
            Node sourceNode = buttonEmail;
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();
            primaryStage.getScene().getRoot().setEffect(null);

            popupStage.close();
        });

        VBox popupLayout = new VBox(20);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.getChildren().addAll(successLabel, closeButton);

        Scene popupScene = new Scene(popupLayout, 300, 100);
        popupStage.setScene(popupScene);
        popupStage.setResizable(false);

        // Obtém a janela principal
        Node sourceNode = buttonEmail;
        Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

        // Aplica o efeito de desfoque à janela principal
        primaryStage.getScene().getRoot().setEffect(new BoxBlur(10, 10, 10));

        popupStage.setOnCloseRequest(e -> {
            // Remove o efeito de desfoque da janela principal ao fechar o pop-up
            primaryStage.getScene().getRoot().setEffect(null);
        });

        popupStage.showAndWait();
    }

}
