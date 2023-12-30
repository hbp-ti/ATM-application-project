package PTDA_ATM;
import SQL.Query;
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
import java.time.LocalDate;
import java.util.*;
import java.net.URL;
import java.io.*;
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
    Query query = new Query();

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

    public void switchToLogIn(ActionEvent event) {
        try {
            String name = textName.getText();
            String NIF = textNIF.getText();
            String address = textAddress.getText();
            String zipCode = textZipCode.getText();
            String phone = textPhone.getText();
            String email = textEmail.getText();
            LocalDate date = textDate.getValue();
            String marital = textMarital.getValue() != null ? textMarital.getValue().toString() : "";
            String gender = textGender.getValue() != null ? textGender.getValue().toString() : "";

            if (!validateRequiredFields()) {
                return;
            }

            if (!validateEmailFormat()) {
                return;
            }

            String accountNumber = query.insertBankAccountData(name, NIF, address, zipCode, phone, email, date, marital, gender);

            String[] cardData = query.insertCardData(accountNumber);
            String cardNumber = cardData[0];
            String cardPIN = cardData[1];

            sendConfirmationEmail(textName.getText(), textEmail.getText(), accountNumber, cardNumber, cardPIN);

            showSuccessPopup(event);

            switchToLogInAfterDelay(event);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("An error occurred while processing your request.");
        }
    }


    private boolean validateRequiredFields() {
        List<TextField> requiredFields = Arrays.asList(textName, textNIF, textAddress, textZipCode, textPhone, textEmail);
        List<ComboBox<String>> requiredComboBoxes = Arrays.asList(textMarital, textGender);
        List<DatePicker> requiredDatePickers = Arrays.asList(textDate);

        boolean isValid = true;
        boolean anyFieldEmpty = false;

        for (TextField field : requiredFields) {
            if (field.getText().isEmpty()) {
                setOrangeBorder(field);
                isValid = false;
                anyFieldEmpty = true;
            } else {
                resetBorder(field);
            }
        }

        for (ComboBox<String> comboBox : requiredComboBoxes) {
            if (comboBox.getValue() == null || comboBox.getValue().isEmpty()) {
                setOrangeBorder(comboBox);
                isValid = false;
                anyFieldEmpty = true;
            } else {
                resetBorder(comboBox);
            }
        }

        // Verifique também o DatePicker
        for (DatePicker datePicker : requiredDatePickers) {
            if (datePicker.getValue() == null) {
                setOrangeBorder(datePicker);
                isValid = false;
                anyFieldEmpty = true;
            } else {
                resetBorder(datePicker);
            }
        }

        if (!isValid && anyFieldEmpty) {
            showError("Please fill in all required fields.");
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

    private void sendConfirmationEmail(String clientName, String email, String accountNumber,String cardNumber,String cardPIN) {
        String emailText = "Dear " + clientName + ",\n\n"
                + "We are delighted to inform you that your bank account has been successfully created at our bank.\n\n"
                + "Below are the details of your new account:\n\n"
                + "Bank account number: " + accountNumber + "\n"
                + "Card number: " + cardNumber + "\n"
                + "Card PIN: " + cardPIN + "\n\n"
                + "Please keep this information in a secure place and do not share it with others.\n\n"
                + "If you have any questions or need assistance, feel free to contact us.\n\n"
                + "Thank you for choosing ByteBank.\n\n"
                + "Best regards,\n"
                + "ByteBank Team";

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