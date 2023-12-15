package atm.ptda_atm;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.image.ImageView;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerMenu {

    @FXML
    private Button buttonLogOut;

    @FXML
    private Pane buttonWithdraw;

    @FXML
    private Pane buttonBalance;

    @FXML
    private Pane buttonTransfer;

    @FXML
    private Pane buttonDeposit;

    @FXML
    private Pane buttonChargePhone;

    @FXML
    private Pane buttonPayment;

    @FXML
    private Pane buttonMiniStatement;

    @FXML
    private Pane buttonChangePIN;

    @FXML
    private Pane buttonOptions;

    @FXML
    private Label labelWelcome;

    @FXML
    private ImageView maleAvatar;

    @FXML
    private ImageView femaleAvatar;

    @FXML
    private ImageView otherAvatar;


    private String clientName;
    private String clientCardNumber;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public void setClientName(String name) {
        this.clientName = name;
        initialize();
    }

    public void setClintCardNumber(String cardNumber) {
        this.clientCardNumber = cardNumber;
    }

    public void initialize() {
        if (clientName != null) {
            labelWelcome.setText("Welcome " + clientName);
        } else {
            labelWelcome.setText("Welcome");
        }

        buttonWithdraw.setOnMouseClicked(mouseEvent -> {
            try {
                switchToWithdraw(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonBalance.setOnMouseClicked(mouseEvent -> {
            try {
                switchToCheckBalance(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonTransfer.setOnMouseClicked(mouseEvent -> {
            try {
                switchToFundTransfer(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonDeposit.setOnMouseClicked(mouseEvent -> {
            try {
                switchToDeposit(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonChargePhone.setOnMouseClicked(mouseEvent -> {
            try {
                switchToChargePhone(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonPayment.setOnMouseClicked(mouseEvent -> {
            try {
                switchToMenuPayment(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonMiniStatement.setOnMouseClicked(mouseEvent -> {
            try {
                switchToMiniStatement(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonChangePIN.setOnMouseClicked(mouseEvent -> {
            try {
                switchToChangePIN(mouseEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        buttonOptions.setOnMouseClicked(mouseEvent -> {
            try {
                switchToOptions(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonLogOut.setOnMouseEntered(e -> buttonLogOut.setCursor(javafx.scene.Cursor.HAND));
        buttonLogOut.setOnMouseExited(e -> buttonLogOut.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonWithdraw.setOnMouseEntered(e -> buttonWithdraw.setCursor(javafx.scene.Cursor.HAND));
        buttonWithdraw.setOnMouseExited(e -> buttonWithdraw.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonBalance.setOnMouseEntered(e -> buttonBalance.setCursor(javafx.scene.Cursor.HAND));
        buttonBalance.setOnMouseExited(e -> buttonBalance.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonTransfer.setOnMouseEntered(e -> buttonTransfer.setCursor(javafx.scene.Cursor.HAND));
        buttonTransfer.setOnMouseExited(e -> buttonTransfer.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonDeposit.setOnMouseEntered(e -> buttonDeposit.setCursor(javafx.scene.Cursor.HAND));
        buttonDeposit.setOnMouseExited(e -> buttonDeposit.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonChargePhone.setOnMouseEntered(e -> buttonChargePhone.setCursor(javafx.scene.Cursor.HAND));
        buttonChargePhone.setOnMouseExited(e -> buttonChargePhone.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonPayment.setOnMouseEntered(e -> buttonPayment.setCursor(javafx.scene.Cursor.HAND));
        buttonPayment.setOnMouseExited(e -> buttonPayment.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonMiniStatement.setOnMouseEntered(e -> buttonMiniStatement.setCursor(javafx.scene.Cursor.HAND));
        buttonMiniStatement.setOnMouseExited(e -> buttonMiniStatement.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonChangePIN.setOnMouseEntered(e -> buttonChangePIN.setCursor(javafx.scene.Cursor.HAND));
        buttonChangePIN.setOnMouseExited(e -> buttonChangePIN.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonOptions.setOnMouseEntered(e -> buttonOptions.setCursor(javafx.scene.Cursor.HAND));
        buttonOptions.setOnMouseExited(e -> buttonOptions.setCursor(javafx.scene.Cursor.DEFAULT));

        String gender = getGenderFromDatabase(clientName);

        if ("Male".equals(gender)) {
            maleAvatar.setVisible(true);
            femaleAvatar.setVisible(false);
            otherAvatar.setVisible(false);
        } else if ("Female".equals(gender)) {
            maleAvatar.setVisible(false);
            femaleAvatar.setVisible(true);
            otherAvatar.setVisible(false);
        } else {
            maleAvatar.setVisible(false);
            femaleAvatar.setVisible(false);
            otherAvatar.setVisible(true);
        }
    }

    // Método que obtém o género da conta ssociada
    private String getGenderFromDatabase(String clientName) {
        String gender = null;

        Connection connection = Conn.getConnection();
        String query = "SELECT gender FROM BankAccount WHERE clientName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, clientName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    gender = resultSet.getString("gender");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro a obter género da base de dados: " + e.getMessage());
        }

        return gender;
    }


    public void switchToLogIn(ActionEvent event) throws IOException {
        Stage stage = (Stage) buttonLogOut.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToChangePIN(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangePIN.fxml"));
        Parent root = loader.load();
        ControllerChangePIN changePINController = loader.getController();
        changePINController.setClintCardNumber(clientCardNumber);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
    }


    public void switchToChargePhone(MouseEvent event) throws IOException {
        Stage stage = (Stage) buttonChargePhone.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("ChargePhone.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCheckBalance(MouseEvent event) throws IOException {
        Stage stage = (Stage) buttonBalance.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("CheckBalance.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDeposit(MouseEvent event) throws IOException {
        Stage stage = (Stage) buttonDeposit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Deposit.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToFundTransfer(MouseEvent event) throws IOException {
        Stage stage = (Stage) buttonTransfer.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("FundTransfer.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMenuPayment(MouseEvent event) throws IOException {
        Stage stage = (Stage) buttonPayment.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("MenuPayment.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMiniStatement(MouseEvent event) throws IOException {
        Stage stage = (Stage) buttonMiniStatement.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("MiniStatement.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToOptions(MouseEvent event) throws IOException {
        Stage stage = (Stage) buttonOptions.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Options.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToWithdraw(MouseEvent event) throws IOException {
        Stage stage = (Stage) buttonWithdraw.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Withdraw.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
