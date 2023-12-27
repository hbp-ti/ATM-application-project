package PTDA_ATM;

import SQL.Query;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.io.*;

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
    Query query = new Query();


    public void initialize() {
        currentPINInput.setOnKeyTyped(event -> clearValidationStyles());
        newPINInput.setOnKeyTyped(event -> clearValidationStyles());
        newPINInput2.setOnKeyTyped(event -> clearValidationStyles());

        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(Cursor.DEFAULT));

        buttonConfirm.setOnMouseEntered(e -> buttonConfirm.setCursor(Cursor.HAND));
        buttonConfirm.setOnMouseExited(e -> buttonConfirm.setCursor(Cursor.DEFAULT));

    }

    public void setClientCardNumber(String clientCardNumber) {
        this.clientCardNumber = clientCardNumber;
        initialize();
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
            if (newPIN.equals(query.getStoredPIN(clientCardNumber))) {
                labelValidacao.setTextFill(Color.RED);
                labelValidacao.setText("The current PIN matches the new PIN!");
                applyValidationStyle();
            } else {
                // Lógica para alterar o PIN no banco de dados
                boolean success = query.changePINInDatabase(clientCardNumber, currentPIN, newPIN);

                if (success) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();

                    // Se a mudança de PIN for bem-sucedida, mostra uma mensagem de sucesso
                    showSuccessPopup("PIN changed successfully!");
                    // Envia email informativo
                    String recipientEmail = query.getClientEmail(clientCardNumber);
                    String subject = "PIN Changed";
                    String messageBody = "Subject: PIN Change Notification for Your Bank Account\n" +
                            "Dear " + query.getClientName(clientCardNumber) + ",\n" +
                            "We would like to inform you that the PIN associated with your bank account's card has been successfully changed processed on "+ formatter.format(now) +".\n" +
                            "If you initiated this change, you can disregard this message. However, if you did not authorize this alteration or if you have any concerns about this update, please contact our bank immediately. We will investigate and resolve this matter promptly.\n" +
                            "The security and protection of your data are of utmost importance to us. We are here to assist and ensure the security of your account.\n" +
                            "Best regards,\n" +
                            "ByteBank\n";
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
