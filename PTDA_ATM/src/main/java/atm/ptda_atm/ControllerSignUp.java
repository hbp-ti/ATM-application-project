package atm.ptda_atm;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.*;
import java.io.*;

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

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToLogIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}