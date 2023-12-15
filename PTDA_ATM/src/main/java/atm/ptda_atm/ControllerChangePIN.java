package atm.ptda_atm;

import javafx.animation.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import javafx.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Objects;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerChangePIN {

    @FXML
    private Label labelValidacao;

    @FXML
    private PasswordField currentPINInput;

    @FXML
    private PasswordField newPINInput;

    @FXML
    private PasswordField newPINInput2;

    @FXML
    private Button buttonGoBack;

    @FXML
    private Button buttonConfirm;

    private String clientCardNumber;
    private PreparedStatement preparedStatement;
    private PreparedStatement preparedStatement2;
    private PreparedStatement preparedStatement3;
    private ResultSet rs;
    private ResultSet rsEmailName;
    private ResultSet rsName;
    private Connection connection;


    public void initialize(Connection connection) {
        currentPINInput.setOnKeyTyped(event -> clearValidationStyles());
        newPINInput.setOnKeyTyped(event -> clearValidationStyles());
        newPINInput2.setOnKeyTyped(event -> clearValidationStyles());

        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(javafx.scene.Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonConfirm.setOnMouseEntered(e -> buttonConfirm.setCursor(javafx.scene.Cursor.HAND));
        buttonConfirm.setOnMouseExited(e -> buttonConfirm.setCursor(javafx.scene.Cursor.DEFAULT));

        this.connection = connection;
    }

    public void setClientCardNumber(String clientCardNumber) {
        this.clientCardNumber = clientCardNumber;
        initialize(connection);
    }

    public void changePIN(ActionEvent event) throws IOException {
        String currentPIN = currentPINInput.getText();
        String newPIN = newPINInput.getText();
        String newPIN2 = newPINInput2.getText();

        // Lógica para verificar se os PINs são válidos
        if (!validatePINs(currentPIN, newPIN, newPIN2)) {
            // Se os PINs não forem válidos, mostra uma mensagem de erro e aplica o estilo de validação
            labelValidacao.setTextFill(Color.RED);
            labelValidacao.setText("Invalid PIN's. Check and try again.");
            applyValidationStyle();
        } else {
            // Lógica para alterar o PIN no banco de dados
            boolean success = changePINInDatabase(clientCardNumber, currentPIN, newPIN);

            if (success) {
                // Se a mudança de PIN for bem-sucedida, mostra uma mensagem de sucesso
                showSuccessPopup("PIN changed successfully!");
                // Envia email informativo
                String recipientEmail = getClientEmail(clientCardNumber);
                String subject = "PIN Change Completed";
                String messageBody = "Your PIN has been changed successfully.";
                sendEmail(recipientEmail, subject, messageBody);

                // Retorna ao menu
                switchToMenu(event);
            } else {
                // Se a mudança de PIN falhar, mostra uma mensagem de erro
                labelValidacao.setTextFill(Color.RED);
                labelValidacao.setText("Current PIN is invalid! Try again.");
                applyValidationStyle();
            }
        }
    }

    private boolean validatePINs(String currentPIN, String newPIN, String newPIN2) {
        // Verifica se os PINs têm o formato correto (por exemplo, contêm apenas números)
        if (!currentPIN.matches("\\d{4}") || !newPIN.matches("\\d{4}") || !newPIN2.matches("\\d{4}")) {
            return false;
        }

        // Verifica se os novos PINs coincidem
        if (!newPIN.equals(newPIN2)) {
            return false;
        }
        return true;
    }

    private boolean changePINInDatabase(String cardNumber, String currentPIN, String newPIN) {
        try {
            String query = "SELECT cardPIN FROM Card WHERE cardNumber = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cardNumber);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String storedPIN = rs.getString("cardPIN").trim();

                // Verifica se o PIN atual corresponde
                if (currentPIN.equals(storedPIN)) {
                    // Atualiza o PIN no banco de dados
                    String updateQuery = "UPDATE Card SET cardPIN = ? WHERE cardNumber = ?";
                    preparedStatement2 = connection.prepareStatement(updateQuery);
                    preparedStatement2.setString(1, newPIN);
                    preparedStatement2.setString(2, cardNumber);
                    preparedStatement2.executeUpdate();

                    return true;
                }
            }
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
                if (preparedStatement2 != null) {
                    preparedStatement2.close();
                }
                if (preparedStatement3 != null) {
                    preparedStatement3.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return false;
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

    private void sendEmail(String recipientEmail, String subject, String text) {
        final String username = "projetoptda@gmail.com";
        final String password = "gcue jaff wcib cklg";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
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

    // Método que trata de mostrar o popup de sucesso a mudar PIN
    private void showSuccessPopup(String message) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Success");

        Label successLabel = new Label(message);
        successLabel.setWrapText(true);
        successLabel.setMaxWidth(Double.MAX_VALUE);
        successLabel.setAlignment(Pos.CENTER);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popupStage.close());

        VBox popupLayout = new VBox(20);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.getChildren().addAll(successLabel, closeButton);

        Scene popupScene = new Scene(popupLayout, 300, 100);
        popupStage.setScene(popupScene);
        popupStage.setResizable(false);

        // Obtém a janela principal
        Node sourceNode = buttonConfirm;
        Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

        // Aplica o efeito de desfoque à janela principal
        primaryStage.getScene().getRoot().setEffect(new BoxBlur(10, 10, 10));

        // Evento ao fechar o pop-up
        popupStage.setOnCloseRequest(e -> {
            // Remove o efeito de desfoque da janela principal
            primaryStage.getScene().getRoot().setEffect(null);
        });

        popupStage.showAndWait();
    }


    // Método para aplicar o estilo de borda laranja
    private void applyValidationStyle() {
        Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
        currentPINInput.setBorder(border);
        newPINInput.setBorder(border);
        newPINInput2.setBorder(border);
    }

    // Método para limpar os estilos de validação
    private void clearValidationStyles() {
        labelValidacao.setText("");
        currentPINInput.setBorder(null);
        newPINInput.setBorder(null);
        newPINInput2.setBorder(null);
    }
}
