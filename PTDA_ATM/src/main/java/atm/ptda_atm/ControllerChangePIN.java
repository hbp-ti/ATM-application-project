package atm.ptda_atm;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
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
import java.util.Properties;

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
    private Connection connection;


    public void initialize() {
        currentPINInput.setOnKeyTyped(event -> clearValidationErrors());
        newPINInput.setOnKeyTyped(event -> clearValidationErrors());
        newPINInput2.setOnKeyTyped(event -> clearValidationErrors());

        buttonGoBack.setOnMouseEntered(e -> buttonGoBack.setCursor(javafx.scene.Cursor.HAND));
        buttonGoBack.setOnMouseExited(e -> buttonGoBack.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonConfirm.setOnMouseEntered(e -> buttonConfirm.setCursor(javafx.scene.Cursor.HAND));
        buttonConfirm.setOnMouseExited(e -> buttonConfirm.setCursor(javafx.scene.Cursor.DEFAULT));
    }

    private void clearValidationErrors() {
        labelValidacao.setText("");
        currentPINInput.setBorder(null);
        newPINInput.setBorder(null);
        newPINInput2.setBorder(null);
    }

    public void setClintCardNumber(String cardNumber) {
        this.clientCardNumber = cardNumber;
    }

    public void changePIN(ActionEvent event) throws IOException {
        try {
            connection = Conn.getConnection();
            preparedStatement = connection.prepareStatement("SELECT cardPIN FROM Card WHERE cardNumber = ?");
            preparedStatement.setString(1, this.clientCardNumber);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String cardPIN = rs.getString("cardPIN");

                if (cardPIN.equals(currentPINInput.getText())) {
                    if (newPINInput.getText().equals(newPINInput2.getText())) {
                        preparedStatement2 = connection.prepareStatement("UPDATE Card SET cardPIN = ? WHERE cardNumber = ?");
                        preparedStatement2.setString(1, newPINInput.getText());
                        preparedStatement2.setString(2, clientCardNumber);

                        int linhasAfetadas = preparedStatement2.executeUpdate();

                        if (linhasAfetadas > 0) {
                            labelValidacao.setTextFill(Color.GREEN);
                            labelValidacao.setText("PIN successfully changed!");
                            Border border = new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
                            currentPINInput.setBorder(border);
                            newPINInput.setBorder(border);
                            newPINInput2.setBorder(border);



                            preparedStatement3 = connection.prepareStatement("SELECT email, clientName FROM BankAccount WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber = ?)");
                            preparedStatement3.setString(1, this.clientCardNumber);

                            rsEmailName = preparedStatement3.executeQuery();

                            if (rsEmailName.next()) {
                                String clientEmail = rsEmailName.getString("email");
                                String clientName = rsEmailName.getString("clientName");

                                String emailText = "Dear " + clientName + " ,\n" +
                                        "We would like to inform you that the PIN associated with your bank account's card has been successfully changed.\n" +
                                        "If you made this change, you may disregard this message. However, if you did not initiate this alteration or if you are unsure about this update, please contact our bank immediately. We will investigate and resolve this matter promptly.\n" +
                                        "The security and protection of your data are paramount to us. We are here to assist and ensure the security of your account.\n" +
                                        "Best regards,\n" +
                                        "ByteBank\n";

                                sendEmail(clientEmail, "Account PIN Change", emailText);

                                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                                pause.setOnFinished(events -> {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
                                    Parent root = null;
                                    try {
                                        root = loader.load();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                    Scene scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.setResizable(false);
                                    stage.show();
                                    stage.centerOnScreen();
                                });
                                pause.play();
                            } else {
                                // Tratar se não houver resultados
                            }
                        } else {
                            labelValidacao.setTextFill(Color.RED);
                            labelValidacao.setText("Update Failed!!");
                            currentPINInput.setText("");
                            newPINInput.setText("");
                            newPINInput2.setText("");
                            Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
                            newPINInput.setBorder(border);
                            newPINInput2.setBorder(border);
                            currentPINInput.setBorder(border);
                        }
                    } else {
                        labelValidacao.setTextFill(Color.RED);
                        labelValidacao.setText("The new PIN does not match in both boxes!");
                        newPINInput.setText("");
                        newPINInput2.setText("");
                        Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
                        newPINInput.setBorder(border);
                        newPINInput2.setBorder(border);
                    }
                } else {
                    labelValidacao.setTextFill(Color.RED);
                    labelValidacao.setText("The current PIN is incorrect!");
                    currentPINInput.setText("");
                    Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
                    currentPINInput.setBorder(border);
                }
            } else {
                // Tratar se não houver resultados
            }
        } catch (SQLException e) {
            System.out.println("SQLExeption: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (rsEmailName != null) {
                    rsEmailName.close();
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
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    public void switchToMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) buttonGoBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
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
}
