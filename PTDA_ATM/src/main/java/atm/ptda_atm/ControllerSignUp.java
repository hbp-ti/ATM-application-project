package atm.ptda_atm;

import javafx.animation.PauseTransition;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;

import java.util.Random;
import java.util.random.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ControllerSignUp {

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
    private Label labelRegisto;

    private Stage stage;
    private Scene scene;
    private Parent root;
    Random random = new Random();
    StringBuilder numeroConta = new StringBuilder();

    public void switchToLogIn(ActionEvent event) throws IOException {
        Conn con = new Conn();
        con.doConnection();

        try (Connection connection = Conn.getConnection()) {
            // Inserção na tabela BankAccount
            try (PreparedStatement preparedStatementBankAccount = connection.prepareStatement("INSERT INTO BankAccount (accountNumber, accountBalance, clientName, NIF, address, zipcode, phoneNumber, email, birthDate, maritalStatus, gender) VALUES (?, 0.00, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
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
                LocalDate localDate = LocalDate.parse((CharSequence) textDate);
                java.sql.Date dataSql = java.sql.Date.valueOf(localDate);
                preparedStatementBankAccount.setDate(8, dataSql);
                //Estado Civil
                preparedStatementBankAccount.setString(9, textMarital.getValue().toString());
                //Genero
                preparedStatementBankAccount.setString(10, textGender.getValue().toString());

                preparedStatementBankAccount.executeUpdate();

                preparedStatementBankAccount.close();
            }

            // Inserção na tabela Card
            try (PreparedStatement preparedStatementCard = connection.prepareStatement("INSERT INTO Card (cardNumber, accountNumber, cardPIN) VALUES (?, ?, ?)")) {

                //Numero Cartao
                StringBuilder numeroCartao = new StringBuilder();
                for (int i = 0; i < 10; i++) {
                    int digito = random.nextInt(10);
                    numeroCartao.append(digito);
                }

                //Numero Conta
                preparedStatementCard.setString(2, numeroCartao.toString());

                //PIN Cartao
                StringBuilder PINCartao = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    int digito = random.nextInt(10);
                    PINCartao.append(digito);
                }
                preparedStatementCard.setString(2, PINCartao.toString());

                preparedStatementCard.executeUpdate();

                preparedStatementCard.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro de conexão: " + e.getMessage());
        }

        labelRegisto.setTextFill(Color.GREEN);
        labelRegisto.setText("Conta criada!");

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(events -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
                stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        pause.play();
    }
}