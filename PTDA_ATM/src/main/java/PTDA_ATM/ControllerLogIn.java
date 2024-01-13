package PTDA_ATM;

import SQL.Query;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
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
     * Número da conta do cliente.
     */
    private String clientAccountNumber;

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
    private Query query = new Query();

    /**
     * Inicializa o controlador. Configura os ouvintes de eventos e interações iniciais.
     */
    public void initialize() {
        // Configuração inicial, como ouvintes de eventos e interações.
        cardNumberInput.setOnKeyTyped(event -> clearValidationErrors());
        passwordInput.setOnKeyTyped(event -> clearValidationErrors());

        signupLink.setOnMouseEntered(e -> signupLink.setCursor(javafx.scene.Cursor.HAND));
        signupLink.setOnMouseExited(e -> signupLink.setCursor(javafx.scene.Cursor.DEFAULT));

        Reflection reflection = new Reflection();
        reflection.setFraction(0.5); // Ajusta o efeito de reflexão

        // Efeito de translação vertical ao passar o mouse
        loginButton.setOnMouseEntered(e -> {
            loginButton.setCursor(javafx.scene.Cursor.HAND);
            loginButton.setTranslateY(2); // Altere o valor conforme necessário
            loginButton.setEffect(reflection); // Adiciona reflexo ao passar o mouse
        });

        loginButton.setOnMouseExited(e -> {
            loginButton.setCursor(javafx.scene.Cursor.DEFAULT);
            loginButton.setTranslateY(0); // Retorna à posição original
            loginButton.setEffect(null); // Remove o efeito de sombra ao passar o mouse
        });
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
                clientAccountNumber = query.getAccountNumber(clientCardNumber);
                this.clientName = query.getClientName(clientAccountNumber);

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
        menuController.setClientAccountNumber(clientAccountNumber);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
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