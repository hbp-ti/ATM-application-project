package PTDA_ATM;

import SQL.Query;
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
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Controlador para a tela de extrato simplificado.
 */
public class ControllerMiniStatement {

    /**
     * Botão para voltar ao menu principal.
     */
    @FXML
    private Button buttonGoBack;

    /**
     * Botão para enviar o extrato por e-mail.
     */
    @FXML
    private Button buttonEmail;

    /**
     * Rótulo para exibir o extrato simplificado.
     */
    @FXML
    private Label miniStatementLabel;

    /**
     * Objeto StringBuilder para armazenar o extrato simplificado.
     */
    private StringBuilder miniStatement = new StringBuilder();

    /**
     * Número da conta do cliente.
     */
    private String clientAccountNumber;

    /**
     * Objeto para executar consultas no banco de dados.
     */
    private final Query query = new Query();

    /**
     * Objeto para criar sombra para os botões
     */
    DropShadow shadow = new DropShadow();

    /**
     * Inicializa o controlador.
     */
    public void initialize() {
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

        buttonEmail.setOnMouseEntered(e -> {
            buttonEmail.setCursor(Cursor.HAND);
            buttonEmail.setTranslateY(2); // Altere o valor conforme necessário
            buttonEmail.setEffect(reflection); // Adiciona reflexo ao passar o mouse
        });
        buttonEmail.setOnMouseExited(e -> {
            buttonEmail.setCursor(Cursor.DEFAULT);
            buttonEmail.setTranslateY(0); // Retorna à posição original
            buttonEmail.setEffect(null); // Remove o efeito de sombra ao passar o mouse

        });
    }

    /**
     * Define o número da conta do cliente.
     *
     * @param clientAccountNumber Número da conta do cliente.
     */
    public void setClientAccountNumber(String clientAccountNumber) {
        this.clientAccountNumber = clientAccountNumber;
        miniStatement = query.loadMiniStatement(clientAccountNumber);
        miniStatementLabel.setText(miniStatement.toString());
        initialize();
    }

    /**
     * Envia o extrato por e-mail.
     *
     * @param event O evento associado à ação.
     * @throws IOException Exceção de entrada/saída.
     */
    public void email(ActionEvent event) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String recipientEmail = query.getClientEmail(clientAccountNumber);
        String subject = "Mini Statement";
        String message = "Subject: Account Statement\n" +
                "Dear " + query.getClientName(clientAccountNumber) + ",\n" +
                "We are pleased to provide your account statement as of " + formatter.format(now) + ":\n\n" +
                miniStatement.toString() + "\n" +
                "Should you have any questions or need further clarification, please do not hesitate to reach out to us. We are here to assist you.\n" +
                "Best regards,\n" +
                "ByteBank";
        sendEmail(recipientEmail, subject, message);

        showSuccessPopup("Email sent successfully!");
    }

    /**
     * Alterna para a tela do menu principal.
     *
     * @param event O evento associado à ação.
     * @throws IOException Exceção de entrada/saída.
     */
    public void switchToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = loader.load();
        ControllerMenu menuController = loader.getController();
        String clientName = query.getClientName(clientAccountNumber);
        menuController.setClientName(clientName);
        menuController.setClientAccountNumber(clientAccountNumber);
        Stage stage = (Stage) buttonGoBack.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Envia um e-mail com o extrato.
     *
     * @param recipientEmail O endereço de e-mail do destinatário.
     * @param subject        O assunto do e-mail.
     * @param text           O corpo do e-mail.
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
     * Exibe uma janela pop-up de sucesso.
     *
     * @param message Mensagem a ser exibida na janela pop-up.
     */
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
