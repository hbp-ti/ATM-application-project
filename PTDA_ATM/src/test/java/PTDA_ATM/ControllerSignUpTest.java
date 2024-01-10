package PTDA_ATM;

import PTDA_ATM.ControllerSignUp;
import PTDA_ATM.ControllerSignUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerSignUpTest {

    private ControllerSignUp controller;

    @BeforeEach
    public void setUp() {
        controller = new ControllerSignUp();
    }

    @AfterEach
    public void tearDown() {
       controller = null;
    }


    @DisplayName("Test: Email Format Validation - Valid Email")
    @Test
    void testValidateEmailFormat_ValidEmail() {
        assertTrue(controller.isValidEmail("johndoe@example.com"), "Valid email format");
    }

    @DisplayName("Test: Email Format Validation - Invalid Email")
    @Test
    void testValidateEmailFormat_InvalidEmail() {
        assertFalse(controller.isValidEmail("invalid-email"), "Invalid email format");
    }

    @DisplayName("Test: Email Format Validation - Null Email")
    @Test
    void testValidateEmailFormat_NullEmail() {
        assertFalse(controller.isValidEmail(""), "Null email");
    }

    @DisplayName("Test: Email Format Validation - Empty Email")
    @Test
    void testValidateEmailFormat_EmptyEmail() {
        assertFalse(controller.isValidEmail(""), "Empty email");
    }
}
