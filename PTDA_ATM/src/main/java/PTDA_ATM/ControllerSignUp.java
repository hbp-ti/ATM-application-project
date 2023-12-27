package PTDA_ATM;

import SQL.Conn;
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
import java.util.*;
import java.net.URL;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private Connection connection = null;

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

        try {
            if (!validateRequiredFields()) {
                return;
            }

            if (!validateEmailFormat()) {
                return;
            }

            connection = Conn.getConnection();
            String accountNumber = insertBankAccountData(connection);
            insertCardData(connection, accountNumber);

            sendConfirmationEmail(textName.getText(), textEmail.getText(), accountNumber);

            showSuccessPopup(event);

            switchToLogInAfterDelay(event);

        } catch (SQLException e) {
            handleSQLException(e);
        } finally {
            closeResources(connection);
        }
    }

    private boolean validateRequiredFields() {
        List<TextField> requiredFields = Arrays.asList(textName, textNIF, textAddress, textZipCode, textPhone, textEmail);
        List<ComboBoxBase> requiredComboBoxes = Arrays.asList(textDate, textMarital, textGender);

        boolean isValid = true;

        for (TextField field : requiredFields) {
            if (field.getText().isEmpty()) {
                showError("Please fill in all required fields.");
                setOrangeBorder(field);
                isValid = false;
            } else {
                resetBorder(field);
            }
        }

        for (ComboBoxBase comboBox : requiredComboBoxes) {
            if (comboBox.getValue() == null) {
                showError("Please fill in all required fields.");
                setOrangeBorder(comboBox);
                isValid = false;
            } else {
                resetBorder(comboBox);
            }
        }

        return isValid;
    }

    private void setOrangeBorder(Control control) {
        control.setStyle("-fx-border-color: orange; -fx-border-radius: 6;");
    }

    private void resetBorder(Control control) {
        control.setStyle(null);
    }

    private boolean validateEmailFormat() {
        if (!isValidEmail(textEmail.getText())) {
            showError("Invalid email format.");
            textEmail.setStyle("-fx-border-color: RED; -fx-border-radius: 6;");
            return false;
        } else {
            textEmail.setStyle(null);
            return true;
        }
    }

    private String insertBankAccountData(Connection connection) throws SQLException {
        PreparedStatement preparedStatementBankAccount = connection.prepareStatement("INSERT INTO BankAccount (accountNumber, clientName, NIF, address, zipcode, phoneNumber, email, birthDate, maritalStatus, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        String accountNumber = generateAccountNumber();
        preparedStatementBankAccount.setString(1, accountNumber);
        preparedStatementBankAccount.setString(2, textName.getText());
        preparedStatementBankAccount.setInt(3, Integer.parseInt(textNIF.getText()));
        preparedStatementBankAccount.setString(4, textAddress.getText());
        preparedStatementBankAccount.setString(5, textZipCode.getText());
        preparedStatementBankAccount.setInt(6, Integer.parseInt(textPhone.getText()));
        preparedStatementBankAccount.setString(7, textEmail.getText());
        preparedStatementBankAccount.setDate(8, java.sql.Date.valueOf(textDate.getValue()));
        preparedStatementBankAccount.setString(9, textMarital.getValue().toString());
        preparedStatementBankAccount.setString(10, textGender.getValue().toString());

        preparedStatementBankAccount.executeUpdate();
        preparedStatementBankAccount.close();

        return accountNumber;
    }

    private void insertCardData(Connection connection, String accountNumber) throws SQLException {
        PreparedStatement preparedStatementCard = connection.prepareStatement("INSERT INTO Card (cardNumber, accountNumber, cardPIN) VALUES (?, ?, ?)");

        String cardNumber = generateCardNumber();
        String cardPIN = generateCardPIN();

        preparedStatementCard.setString(1, cardNumber);
        preparedStatementCard.setString(2, accountNumber);
        preparedStatementCard.setString(3, cardPIN);

        preparedStatementCard.executeUpdate();
        preparedStatementCard.close();
    }

    private void sendConfirmationEmail(String clientName, String email, String accountNumber) {
        // Envie o e-mail com as informações da conta e do cartão
        String emailText = null;
        try {
            emailText = "Dear " + clientName + ",\n\n"
                    + "We are delighted to inform you that your bank account has been successfully created at our bank.\n\n"
                    + "Below are the details of your new account:\n\n"
                    + "Bank account number: " + accountNumber + "\n"
                    + "Card number: " + generateCardNumber() + "\n"
                    + "Card PIN: " + generateCardPIN() + "\n\n"
                    + "Please keep this information in a secure place and do not share it with others.\n\n"
                    + "If you have any questions or need assistance, feel free to contact us.\n\n"
                    + "Thank you for choosing ByteBank.\n\n"
                    + "Best regards,\n"
                    + "ByteBank Team";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        sendEmail(email, "Account creation", emailText);
    }

    private void showSuccessPopup(ActionEvent event) {
        // Obtém a janela principal
        Node sourceNode = (Node) event.getSource();
        Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

        // Aplica o efeito de desfoque à janela principal
        primaryStage.getScene().getRoot().setEffect(new BoxBlur(10, 10, 10));

        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Success!");

        Label messageLabel = new Label("Your account has been created successfully!\n" +
                "We have sent an email to:\n" + textEmail.getText() + " with your account and card information!");

        messageLabel.setWrapText(true);
        messageLabel.setPrefWidth(300);
        messageLabel.setMaxHeight(Double.MAX_VALUE);

        VBox.setMargin(messageLabel, new Insets(10, 10, 50, 10));

        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> {
            // Remove o efeito de desfoque da janela principal
            primaryStage.getScene().getRoot().setEffect(null);
            popupWindow.close();
        });
        closeButton.setPrefWidth(80);
        closeButton.setPrefHeight(30);
        VBox.setMargin(closeButton, new Insets(0, 10, 10, 10));

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(messageLabel, closeButton);

        Scene popupScene = new Scene(layout, 400, 200);
        popupWindow.setResizable(false);
        popupWindow.setScene(popupScene);

        popupWindow.showAndWait();
    }



    private void switchToLogInAfterDelay(ActionEvent event) {
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

    private String generateAccountNumber() throws SQLException {
        while (true) {
            String accountNumber = generateRandomNumber(20);
            if (!isAccountNumberExists(accountNumber)) {
                return accountNumber;
            }
        }
    }

    private String generateCardNumber() throws SQLException {
        while (true) {
            String cardNumber = generateRandomNumber(10);
            if (!isCardNumberExists(cardNumber)) {
                return cardNumber;
            }
        }
    }

    private String generateRandomNumber(int length) {
        StringBuilder number = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            number.append(digit);
        }
        return number.toString();
    }

    private boolean isAccountNumberExists(String accountNumber) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM BankAccount WHERE accountNumber = ?");
        preparedStatement.setString(1, accountNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count > 0;
    }

    private boolean isCardNumberExists(String cardNumber) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM Card WHERE cardNumber = ?");
        preparedStatement.setString(1, cardNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count > 0;
    }


    private String generateCardPIN() {
        StringBuilder cardPIN = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10);
            cardPIN.append(digit);
        }
        return cardPIN.toString();
    }

    private void handleSQLException(SQLException e) {
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
    }

    private void closeResources(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
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