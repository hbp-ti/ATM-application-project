package PTDA_ATM;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ControllerSignUpTest {

    private ControllerSignUp controller;

    @BeforeEach
    void setUp() {
        controller = new ControllerSignUp();
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }

    @Test
    @DisplayName("Valid Email Format")
    void testValidEmailFormat() {
        assertTrue(controller.isValidEmail("johndoe@example.com"), "Valid email format");
    }

    @Test
    @DisplayName("Invalid Email Format")
    void testInvalidEmailFormat() {
        assertFalse(controller.isValidEmail("invalid-email"), "Invalid email format");
    }

    @Test
    @DisplayName("Null Email Format")
    void testNullEmailFormat() {
        assertFalse(controller.isValidEmail(""), "Null email");
    }

    @Test
    @DisplayName("Empty Email Format")
    void testEmptyEmailFormat() {
        assertFalse(controller.isValidEmail(""), "Empty email");
    }
}
