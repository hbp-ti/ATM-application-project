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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerSignUp implements Initializable {

    @FXML
    private TextField textName;

    @FXML
    private TextField textEmail;

    @FXML
    private DatePicker textDate;

    @FXML
    private TextField textAddress;

    @FXML
    private TextField textZipCode;

    @FXML
    private TextField textPhone;

    @FXML
    private ComboBox textGender;

    @FXML
    private ComboBox textMarital;

    @FXML
    private TextField textNIF;

    @FXML
    private Button buttonRegister;

    @FXML
    private ImageView goBackArrow;


    private Stage stage;
    private Scene scene;
    private Parent root;
    Random random = new Random();
    StringBuilder numeroConta = new StringBuilder();
    private PreparedStatement preparedStatement;
    private ResultSet rs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Preenche as ComboBoxes com os valores
        textGender.getItems().addAll("Male", "Female", "Other");
        textMarital.getItems().addAll("Married", "Divorced", "Single", "Widower");

        // Adiciona o efeito de mudança de cursor para o botão de login
        buttonRegister.setOnMouseEntered(e -> buttonRegister.setCursor(javafx.scene.Cursor.HAND));
        buttonRegister.setOnMouseExited(e -> buttonRegister.setCursor(javafx.scene.Cursor.DEFAULT));

        // Adiciona o efeito de mudança de cursor para o hyperlink de signup
        goBackArrow.setOnMouseEntered(e -> goBackArrow.setCursor(javafx.scene.Cursor.HAND));
        goBackArrow.setOnMouseExited(e -> goBackArrow.setCursor(javafx.scene.Cursor.DEFAULT));

        // Adiciona o ouvinte para o evento KeyReleased no campo de e-mail
        textEmail.setOnKeyReleased(this::handleEmailKeyReleased);
    }

    public void switchLogInArrow(MouseEvent event) throws IOException {
        // Obtém a janela principal
        Node sourceNode = (Node) event.getSource();
        Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

        // Aplica o efeito de desfoque à janela principal
        primaryStage.getScene().getRoot().setEffect(new BoxBlur(10, 10, 10));

        Stage confirmationWindow = new Stage();
        confirmationWindow.initModality(Modality.APPLICATION_MODAL);
        confirmationWindow.setTitle("Confirmation");

        Label confirmationLabel = new Label("Are you sure you want to cancel the registration?");
        confirmationLabel.setWrapText(true);
        confirmationLabel.setMaxWidth(Double.MAX_VALUE);
        confirmationLabel.setAlignment(Pos.CENTER);

        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            stage.centerOnScreen();

            // Remove o efeito de desfoque da janela principal
            primaryStage.getScene().getRoot().setEffect(null);

            confirmationWindow.close();
        });
        yesButton.setPrefWidth(80);
        yesButton.setPrefHeight(30);

        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
            // Remove o efeito de desfoque da janela principal
            primaryStage.getScene().getRoot().setEffect(null);

            confirmationWindow.close();
        });
        noButton.setPrefWidth(80);
        noButton.setPrefHeight(30);

        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(yesButton, noButton);

        VBox confirmationLayout = new VBox(20); // Ajuste o espaçamento vertical conforme necessário
        confirmationLayout.setAlignment(Pos.CENTER);
        confirmationLayout.getChildren().addAll(confirmationLabel, buttonLayout);

        Scene confirmationScene = new Scene(confirmationLayout, 300, 100); // Ajuste as dimensões conforme necessário
        confirmationWindow.setScene(confirmationScene);
        confirmationWindow.setResizable(false);

        confirmationWindow.showAndWait();
    }




    public void switchToLogIn(ActionEvent event) throws IOException {
        Conn con = new Conn();
        Connection connection = null;

        try  {
            // Validar campos obrigatórios
            if (textName.getText().isEmpty() || textNIF.getText().isEmpty() || textAddress.getText().isEmpty() ||
                    textZipCode.getText().isEmpty() || textPhone.getText().isEmpty() || textEmail.getText().isEmpty() ||
                    textDate.getValue() == null || textMarital.getValue() == null || textGender.getValue() == null) {

                // Exibir mensagem de erro
                showError("Please fill in all required fields.");

                // Adicionar borda laranja às caixas de texto não preenchidas
                if (textName.getText().isEmpty()) {
                    textName.setStyle("-fx-border-color: orange; -fx-border-radius: 6;");
                } else {
                    textName.setStyle(null); // Remover a borda se o campo estiver preenchido
                }

                if (textNIF.getText().isEmpty()) {
                    textNIF.setStyle("-fx-border-color: orange; -fx-border-radius: 6;");
                } else {
                    textNIF.setStyle(null);
                }

                if (textAddress.getText().isEmpty()) {
                    textAddress.setStyle("-fx-border-color: orange; -fx-border-radius: 6;");
                } else {
                    textAddress.setStyle(null);
                }

                if (textZipCode.getText().isEmpty()) {
                    textZipCode.setStyle("-fx-border-color: orange; -fx-border-radius: 6;");
                } else {
                    textZipCode.setStyle(null);
                }

                if (textPhone.getText().isEmpty()) {
                    textPhone.setStyle("-fx-border-color: orange; -fx-border-radius: 6;");
                } else {
                    textPhone.setStyle(null);
                }

                if (textEmail.getText().isEmpty()) {
                    textEmail.setStyle("-fx-border-color: orange; -fx-border-radius: 6;");
                } else {
                    textEmail.setStyle(null);
                }

                if (Objects.isNull(textDate.getValue())) {
                    textDate.setStyle("-fx-border-color: orange; -fx-border-radius: 6;");
                } else {
                    textDate.setStyle(null);
                }

                if (Objects.isNull(textMarital.getValue())) {
                    textMarital.setStyle("-fx-border-color: orange; -fx-border-radius: 6;");
                } else {
                    textMarital.setStyle(null);
                }

                if (Objects.isNull(textGender.getValue())) {
                    textGender.setStyle("-fx-border-color: orange; -fx-border-radius: 6;");
                } else {
                    textGender.setStyle(null);
                }

                return;

            } else {

                // Validar o formato do endereço de e-mail
                if (!isValidEmail(textEmail.getText())) {
                    showError("Invalid email format.");
                    textEmail.setStyle("-fx-border-color: RED; -fx-border-radius: 6;");

                    return;
                }

                // Remover a formatação vermelha se o e-mail for válido
                textEmail.setStyle(null);

                connection = Conn.getConnection();
                // Inserção na tabela BankAccount
                PreparedStatement preparedStatementBankAccount = connection.prepareStatement("INSERT INTO BankAccount (accountNumber, accountBalance, clientName, NIF, address, zipcode, phoneNumber, email, birthDate, maritalStatus, gender) VALUES (?, 0.00, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                //Numero conta
                for (int i = 0; i < 20; i++) {
                    int digito = random.nextInt(10);
                    numeroConta.append(digito);
                }
                preparedStatementBankAccount.setString(1, numeroConta.toString());

                //Nome
                preparedStatementBankAccount.setString(2, textName.getText());
                //NIF
                preparedStatementBankAccount.setInt(3, Integer.parseInt(textNIF.getText()));
                //Morada
                preparedStatementBankAccount.setString(4, textAddress.getText());
                //Codigo Postal
                preparedStatementBankAccount.setString(5, textZipCode.getText());
                //Numero Telemovel
                preparedStatementBankAccount.setInt(6, Integer.parseInt(textPhone.getText()));
                //Email
                preparedStatementBankAccount.setString(7, textEmail.getText());
                //DataNascimento
                LocalDate localDate = textDate.getValue();
                java.sql.Date dataSql = java.sql.Date.valueOf(localDate);
                preparedStatementBankAccount.setDate(8, dataSql);
                //Estado Civil
                preparedStatementBankAccount.setString(9, textMarital.getValue().toString());
                //Genero
                preparedStatementBankAccount.setString(10, textGender.getValue().toString());

                preparedStatementBankAccount.executeUpdate();

                preparedStatementBankAccount.close();





                PreparedStatement preparedStatementCard = connection.prepareStatement("INSERT INTO Card (cardNumber, accountNumber, cardPIN) VALUES (?, ?, ?)");

                //Numero Cartao
                StringBuilder numeroCartao = new StringBuilder();
                for (int i = 0; i < 10; i++) {
                    int digito = random.nextInt(10);
                    numeroCartao.append(digito);
                }
                preparedStatementCard.setString(1, numeroCartao.toString());

                //Numero Conta
                preparedStatementCard.setString(2, numeroConta.toString());

                //PIN Cartao
                StringBuilder PINCartao = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    int digito = random.nextInt(10);
                    PINCartao.append(digito);
                }
                preparedStatementCard.setString(3, PINCartao.toString());

                preparedStatementCard.executeUpdate();

                preparedStatementCard.close();


                //Envia um email com as informacoes da conta e do cartao
                String emailText = "Dear " + textName.getText() + ",\n\n"
                        + "We are delighted to inform you that your bank account has been successfully created at our bank.\n\n"
                        + "Below are the details of your new account:\n\n"
                        + "Bank account number: " + numeroConta.toString() + "\n"
                        + "Card number: " + numeroCartao.toString() + "\n"
                        + "Card PIN: " + PINCartao.toString() + "\n\n"
                        + "Please keep this information in a secure place and do not share it with others.\n\n"
                        + "If you have any questions or need assistance, feel free to contact us.\n\n"
                        + "Thank you for choosing ByteBank.\n\n"
                        + "Best regards,\n"
                        + "ByteBank Team";


                sendEmail(textEmail.getText(), "Account creation", emailText);


                // Obtém a janela principal
                Node sourceNode = (Node) event.getSource();
                Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

                // Aplica o efeito de desfoque à janela principal
                primaryStage.getScene().getRoot().setEffect(new BoxBlur(10, 10, 10));

                Stage popupWindow = new Stage();
                popupWindow.initModality(Modality.APPLICATION_MODAL);
                popupWindow.setTitle("Success!");

                Label messageLabel = new Label("Your account has been created successfully!\n" +
                        "We have sent an email to:\n" + textEmail.getText() + " with your account and card informations!");

                messageLabel.setWrapText(true);
                messageLabel.setPrefWidth(300);
                messageLabel.setMaxHeight(Double.MAX_VALUE);

                VBox.setMargin(messageLabel, new javafx.geometry.Insets(10, 10, 50, 10));

                Button closeButton = new Button("OK");
                closeButton.setOnAction(e -> {
                    // Remove o efeito de desfoque da janela principal
                    primaryStage.getScene().getRoot().setEffect(null);
                    popupWindow.close();
                });
                closeButton.setPrefWidth(80);
                closeButton.setPrefHeight(30);
                VBox.setMargin(closeButton, new javafx.geometry.Insets(0, 10, 10, 10));

                VBox layout = new VBox(10);
                layout.setAlignment(Pos.CENTER);
                layout.getChildren().addAll(messageLabel, closeButton);

                Scene popupScene = new Scene(layout, 400, 200);
                popupWindow.setResizable(false);
                popupWindow.setScene(popupScene);

                popupWindow.showAndWait();




                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(events -> {
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
                        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                        stage.centerOnScreen();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                pause.play();
            }

        } catch (SQLException e) {
            System.out.println("SQLExeption: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (connection != null && !connection.isClosed()) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                System.err.println("Error closing resources: " + e.getMessage());
//            }
        }
    }

    // Método que envia email
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

    // Método que valida o email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private void handleEmailKeyReleased(KeyEvent event) {
        // Remove a formatação vermelha quando o usuário pressiona uma tecla no campo de e-mail
        textEmail.setStyle(null);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Mostrar o Alert
        alert.showAndWait();
    }
}