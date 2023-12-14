package atm.ptda_atm;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Label;
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
    private AnchorPane buttonLogOut;

    @FXML
    private Pane buttonWithdraw;

    @FXML
    private Pane buttonBalance;

    @FXML
    private Pane buttonTransfer;

    @FXML
    private Pane buttonDeposit;

    @FXML
    private Pane buttonChangePIN;

    @FXML
    private Pane buttonPayment;

    @FXML
    private Pane buttonMiniStatement;

    @FXML
    private Label labelWelcome;

    @FXML
    private ImageView maleAvatar;

    @FXML
    private ImageView femaleAvatar;


    private String clientName;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public void setClientName(String name) {
        this.clientName = name;
        initialize();
    }

    public void initialize() {
        if (clientName != null) {
            labelWelcome.setText("Welcome " + clientName);
        } else {
            labelWelcome.setText("Welcome");
        }

        buttonLogOut.setOnMouseClicked(mouseEvent -> {
            try {
                // Criando um ActionEvent vazio
                ActionEvent actionEvent = new ActionEvent();
                switchToLogIn(actionEvent);
            } catch (IOException e) {
                e.printStackTrace(); // Lida com exceções, você pode modificar conforme necessário
            }
        });

        buttonLogOut.setOnMouseEntered(e -> buttonLogOut.setCursor(javafx.scene.Cursor.HAND));
        buttonLogOut.setOnMouseExited(e -> buttonLogOut.setCursor(javafx.scene.Cursor.DEFAULT));

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

        buttonChangePIN.setOnMouseEntered(e -> buttonChangePIN.setCursor(javafx.scene.Cursor.HAND));
        buttonChangePIN.setOnMouseExited(e -> buttonChangePIN.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonPayment.setOnMouseEntered(e -> buttonPayment.setCursor(javafx.scene.Cursor.HAND));
        buttonPayment.setOnMouseExited(e -> buttonPayment.setCursor(javafx.scene.Cursor.DEFAULT));

        buttonMiniStatement.setOnMouseEntered(e -> buttonMiniStatement.setCursor(javafx.scene.Cursor.HAND));
        buttonMiniStatement.setOnMouseExited(e -> buttonMiniStatement.setCursor(javafx.scene.Cursor.DEFAULT));

        String gender = getGenderFromDatabase(clientName);

        if ("Male".equals(gender)) {
            maleAvatar.setVisible(true);
            femaleAvatar.setVisible(false);
        } else if ("Female".equals(gender)) {
            maleAvatar.setVisible(false);
            femaleAvatar.setVisible(true);
        } else {
            // Se o gênero não for especificado, talvez você queira ocultar ambos ou tomar alguma outra ação
            maleAvatar.setVisible(false);
            femaleAvatar.setVisible(false);
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
}
