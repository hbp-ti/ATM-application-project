package PTDA_ATM;

import SQL.Query;
import javafx.animation.PauseTransition;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.*;

/**
 * Controlador responsável pela lógica da interface de login.
 */
public class ControllerLogIn {

    /**
     * Campo de entrada para o número do cartão do cliente.
     */
    @FXML
    private TextField cardNumberInput;

    /**
     * Campo de entrada para a senha do cliente.
     */
    @FXML
    private PasswordField passwordInput;

    /**
     * Botão de login.
     */
    @FXML
    private Button loginButton;

    /**
     * Rótulo para exibir mensagens de validação ou erro.
     */
    @FXML
    private Label labelValidation;

    /**
     * Hiperlink para a página de cadastro.
     */
    @FXML
    private Hyperlink signupLink;

    /**
     * Imagem do logotipo do banco.
     */
    @FXML
    private ImageView bankLogo;

    /**
     * Palco da aplicação.
     */
    private Stage stage;

    /**
     * Cena da aplicação.
     */
    private Scene scene;

    /**
     * Número do cartão do cliente.
     */
    private String clientCardNumber;

    /**
     * Senha do cliente.
     */
    private String password;

    /**
     * Nome do cliente.
     */
    private String clientName;

    /**
     * Objeto para executar consultas no banco de dados.
     */
    Query query = new Query();

    /**
     * Inicializa o controlador. Configura os ouvintes de eventos e interações iniciais.
     */
    public void initialize() {
        // Configuração inicial, como ouvintes de eventos e interações.
        cardNumberInput.setOnKeyTyped(event -> clearValidationErrors());
        passwordInput.setOnKeyTyped(event -> clearValidationErrors());

        loginButton.setOnMouseEntered(e -> loginButton.setCursor(javafx.scene.Cursor.HAND));
        loginButton.setOnMouseExited(e -> loginButton.setCursor(javafx.scene.Cursor.DEFAULT));

        signupLink.setOnMouseEntered(e -> signupLink.setCursor(javafx.scene.Cursor.HAND));
        signupLink.setOnMouseExited(e -> signupLink.setCursor(javafx.scene.Cursor.DEFAULT));
    }

    /**
     * Realiza a transição para a página principal após a validação bem-sucedida do login.
     *
     * @param event O evento associado ao botão de login.
     * @throws IOException Exceção lançada se houver um problema durante a transição para a página principal.
     */
    public void switchToMainPage(ActionEvent event){
        // Lógica de validação e transição para a página principal.
        clientCardNumber = cardNumberInput.getText();
        password = passwordInput.getText();

        boolean verifyCard = query.verifyCardInfo(clientCardNumber,password);

        if (verifyCard) {
                labelValidation.setText("Valid Data!");
                applyCorrectStyle();
                this.clientName = query.getClientName(clientCardNumber);

                PauseTransition pauseValidation = new PauseTransition(Duration.seconds(2));
                pauseValidation.setOnFinished(events -> {
                    try {
                        switchToMenu(event);
                    } catch (IOException es) {
                        es.printStackTrace();
                    }
                });
                pauseValidation.play();
        } else {
            labelValidation.setText("Invalid data!");
            passwordInput.setText("");
            applyValidationStyle();
        }
    }

    /**
     * Realiza a transição para a página do menu após o login bem-sucedido.
     *
     * @param event O evento associado ao botão de menu.
     * @throws IOException Exceção lançada se houver um problema durante a transição para a página do menu.
     */
    public void switchToMenu(ActionEvent event) throws IOException {
        // Lógica para transição para a página do menu.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = loader.load();
        ControllerMenu menuController = loader.getController();
        menuController.setClientCardNumber(clientCardNumber);
        menuController.setClientName(clientName);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Realiza a transição para a página de registro.
     *
     * @param event O evento associado ao link de registro.
     * @throws IOException Exceção lançada se houver um problema durante a transição para a página de registo.
     */
    public void switchToSignUp(ActionEvent event) throws IOException {
        // Lógica para transição para a página de registo.
        Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Aplica o estilo de validação, destacando campos com borda vermelha.
     */
    private void applyValidationStyle() {
        // Lógica para aplicar estilo de validação.
        labelValidation.setTextFill(Color.RED);
        Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
        cardNumberInput.setBorder(border);
        passwordInput.setBorder(border);
    }

    /**
     * Aplica o estilo correto, destacando campos com borda verde.
     */
    private void applyCorrectStyle() {
        // Lógica para aplicar estilo correto.
        labelValidation.setTextFill(Color.GREEN);
        Border border = new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT));
        cardNumberInput.setBorder(border);
        passwordInput.setBorder(border);
    }

    /**
     * Limpa os erros de validação, removendo qualquer destaque.
     */
    private void clearValidationErrors() {
        // Lógica para limpar erros de validação.
        labelValidation.setText("");
        cardNumberInput.setBorder(null);
        passwordInput.setBorder(null);
    }
}