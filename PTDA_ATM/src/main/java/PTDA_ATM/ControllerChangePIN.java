package PTDA_ATM;

import SQL.Query;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
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

/**
 * Controlador para a tela de alteração de PIN.
 */
public class ControllerChangePIN {

    /**
     * Rótulo para exibir mensagens de validação ou erro.
     */
    @FXML
    private Label labelValidacao;

    /**
     * Campo de senha para inserir o PIN atual.
     */
    @FXML
    private PasswordField currentPINInput;

    /**
     * Campo de senha para inserir o novo PIN.
     */
    @FXML
    private PasswordField newPINInput;

    /**
     * Campo de senha para confirmar o novo PIN.
     */
    @FXML
    private PasswordField newPINInput2;

    /**
     * Botão para retornar à tela de menu.
     */
    @FXML
    private Button buttonGoBack;

    /**
     * Botão para confirmar a alteração de PIN.
     */
    @FXML
    private Button buttonConfirm;

    /**
     * Número do cartão do cliente.
     */
    private String clientCardNumber;

    /**
     * Objeto para executar consultas no banco de dados.
     */
    Query query = new Query();

    /**
     * Objeto para criar sombra para os botões
     */
    DropShadow shadow = new DropShadow();

    /**
     * Método de inicialização do controlador.
     * Configura os eventos e estilos para a tela de alteração de PIN.
     */
    public void initialize() {
        currentPINInput.setOnKeyTyped(event -> clearValidationStyles());
        newPINInput.setOnKeyTyped(event -> clearValidationStyles());
        newPINInput2.setOnKeyTyped(event -> clearValidationStyles());

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

        buttonConfirm.setOnMouseEntered(e -> {
            buttonConfirm.setCursor(Cursor.HAND);
            buttonConfirm.setTranslateY(2); // Altere o valor conforme necessário
            buttonConfirm.setEffect(reflection); // Adiciona reflexo ao passar o mouse
        });
        buttonConfirm.setOnMouseExited(e -> {
            buttonConfirm.setCursor(Cursor.DEFAULT);
            buttonConfirm.setTranslateY(0); // Retorna à posição original
            buttonConfirm.setEffect(null); // Remove o efeito de sombra ao passar o mouse
        });

    }

    /**
     * Define o número do cartão do cliente.
     *
     * @param clientCardNumber O número do cartão do cliente.
     */
    public void setClientCardNumber(String clientCardNumber) {
        this.clientCardNumber = clientCardNumber;
        initialize();
    }

    /**
     * Método chamado ao confirmar a alteração de PIN.
     *
     * @param event O evento associado à ação.
     * @throws IOException Exceção de entrada/saída.
     */
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

    /**
     * Valida os PINs inseridos.
     *
     * @param currentPIN O PIN atual.
     * @param newPIN     O novo PIN.
     * @param newPIN2    A confirmação do novo PIN.
     * @return True se os PINs são válidos, False caso contrário.
     */
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

    /**
     * Altera para a tela de menu.
     *
     * @param event O evento associado à ação.
     * @throws IOException Exceção de entrada/saída.
     */
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

    /**
     * Envia um email.
     *
     * @param recipientEmail O endereço de email do destinatário.
     * @param subject        O assunto do email.
     * @param text           O corpo do email.
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
     * Mostra um pop-up de sucesso.
     *
     * @param message A mensagem de sucesso a ser exibida.
     */
    private void showSuccessPopup(String message) {
        // Método que trata de mostrar o popup de sucesso a mudar PIN
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

    /**
     * Mostra uma mensagem de erro em um Alert.
     *
     * @param message A mensagem de erro a ser exibida.
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
     * Aplica o estilo de borda vermelha para indicar validação inválida.
     */
    private void applyValidationStyle() {
        Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
        currentPINInput.setBorder(border);
        newPINInput.setBorder(border);
        newPINInput2.setBorder(border);
    }

    /**
     * Limpa os estilos de validação.
     */
    private void clearValidationStyles() {
        labelValidacao.setText("");
        currentPINInput.setBorder(null);
        newPINInput.setBorder(null);
        newPINInput2.setBorder(null);
    }
}
