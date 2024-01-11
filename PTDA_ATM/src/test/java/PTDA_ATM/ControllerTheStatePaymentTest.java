package PTDA_ATM;

import org.junit.jupiter.api.*;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class ControllerTheStatePaymentTest {

    private ControllerTheStatePayment controller;

    @BeforeEach
    void setUp() {
        controller = new ControllerTheStatePayment();
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }

    @Test
    @DisplayName("Valid Payment")
    void testValidPayment() {
        assertTrue(controller.validatePayment("123456789012345", "124.12"), "Expected a valid payment, but returned false.");
    }

    @Test
    @DisplayName("Reference Not Found")
    void testReferenceNotFound() {
        assertFalse(controller.validatePayment("invalidReference", "100.00"), "Expected a reference not found, but returned true.");
    }

    @Test
    @DisplayName("Amount Mismatch")
    void testAmountMismatch() {
        assertFalse(controller.validatePayment("validReference", "50.00"), "Expected an amount mismatch, but returned true.");
    }

    @Test
    @DisplayName("Incorrect Input Types")
    void testIncorrectInputTypes() {
        assertFalse(controller.validatePayment("1", "2"), "Expected incorrect input types, but returned true.");
    }

    @Test
    @DisplayName("Not Null HashMap")
    void testNotNullHashMap() {
        assertNotNull(controller.getHashMap(), "Expected a non-null HashMap, but received null.");
    }

    @Test
    @DisplayName("Valid Input")
    void testValidInput() {
        assertTrue(controller.validateInput("123456789012345", "100.00"), "Expected valid input, but returned false.");
    }

    @Test
    @DisplayName("Invalid Reference Length")
    void testInvalidReferenceLength() {
        assertFalse(controller.validateInput("1234", "100.00"), "Expected invalid reference length, but returned true.");
    }

    @Test
    @DisplayName("Invalid Amount Format")
    void testInvalidAmountFormat() {
        assertFalse(controller.validateInput("123456789012345", "invalid"), "Expected invalid amount format, but returned true.");
    }
}
