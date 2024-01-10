package PTDA_ATM;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerChangePINTest {
    private ControllerChangePIN controller;
    @BeforeEach
    void setUp() {
        controller = new ControllerChangePIN();
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }

    @Test
    @DisplayName("Valid PINs")
    public void testValidPINs() {
        boolean isValid = controller.validatePINs("1234", "5678", "5678");
        assertTrue(isValid, "Expected valid PINs, but returned false.");
    }

    @Test
    @DisplayName("Invalid Format: Non-Numeric")
    public void testInvalidFormat_NonNumeric() {
        boolean isValid = controller.validatePINs("12a4", "5678", "5678");
        assertFalse(isValid, "Expected invalid format (non-numeric), but returned true.");
    }

    @Test
    @DisplayName("Invalid Format: Length")
    public void testInvalidFormat_Length() {
        boolean isValid = controller.validatePINs("12345", "5678", "5678");
        assertFalse(isValid, "Expected invalid format (wrong length), but returned true.");
    }

    @Test
    @DisplayName("Mismatched New PINs")
    public void testMismatchedNewPINs() {
        boolean isValid = controller.validatePINs("1234", "5678", "9876");
        assertFalse(isValid, "Expected mismatched new PINs, but returned true.");
    }
}