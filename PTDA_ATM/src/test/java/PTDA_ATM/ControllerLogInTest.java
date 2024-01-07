package PTDA_ATM;
import SQL.Query;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.scene.control.Label;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ControllerLogInTest {
    private ControllerLogIn controller = new ControllerLogIn();

    @BeforeEach
    void setUp() {
         controller = new ControllerLogIn();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testSwitchToMainPage_EmptyFields() throws IOException {
        controller.labelValidation = new Label();
        controller.cardNumberInput = new TextField();
        controller.passwordInput = new PasswordField();
        controller.query = new Query(); // Assuming Query has required methods

        // Mocking empty fields
        controller.cardNumberInput.setText("");
        controller.passwordInput.setText("");

        controller.switchToMainPage(null);

        assertEquals("Invalid data!", controller.labelValidation.getText());
    }

    @Test
    public void testSwitchToMainPage_NullFields() throws IOException {

        controller.labelValidation = new Label();
        controller.query = new Query(); // Assuming Query has required methods

        controller.switchToMainPage(null);

        assertEquals("Invalid data!", controller.labelValidation.getText());
    }

//    @Test
//    public void testSwitchToMainPage_NullValidationLabel() throws IOException {
//
//        // Nullifying the validation label intentionally
//
//        controller.switchToMainPage(null);
//
//        // No assertion as we're just testing the absence of exceptions
//    }

    @Test
    public void testSwitchToMenu_ExceptionThrown() throws IOException {
        ControllerLogIn controller = new ControllerLogIn();
        controller.query = new Query(); // Assuming Query has required methods

        controller.clientAccountNumber = "123456789"; // Mocking account number
        controller.clientName = "Test User"; // Mocking client name

        // Simulating an exception during the transition
        assertThrows(IOException.class, () -> controller.switchToMenu(null));
    }
}