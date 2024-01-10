package PTDA_ATM;

import org.junit.jupiter.api.*;
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
    void testValidPINs() {
        assertTrue(controller.validatePINs("1234", "5678", "5678"), "Expected valid PINs, but returned false.");
    }

    @Test
    @DisplayName("Invalid Format: Non-Numeric")
    void testInvalidFormat_NonNumeric() {
        assertFalse(controller.validatePINs("12a4", "5678", "5678"), "Expected invalid format (non-numeric), but returned true.");
    }

    @Test
    @DisplayName("Invalid Format: Length")
    void testInvalidFormat_Length() {
        assertFalse(controller.validatePINs("12345", "5678", "5678"), "Expected invalid format (wrong length), but returned true.");
    }

    @Test
    @DisplayName("Mismatched New PINs")
    void testMismatchedNewPINs() {
        assertFalse(controller.validatePINs("1234", "5678", "9876"), "Expected mismatched new PINs, but returned true.");
    }
}
