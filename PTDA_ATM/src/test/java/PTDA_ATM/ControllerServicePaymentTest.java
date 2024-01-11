package PTDA_ATM;

import org.junit.jupiter.api.*;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class ControllerServicePaymentTest {

    private ControllerServicePayment controller;

    @BeforeEach
    void setUp() {
        controller = new ControllerServicePayment();
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }

    @Test
    @DisplayName("Valid Payment")
    void testValidPayment() {
        assertTrue(controller.validatePayment("12345", "123456789", "123.12"), "Expected valid payment, but returned false.");
    }

    @Test
    @DisplayName("Reference Not Found")
    void testReferenceNotFound() {
        assertFalse(controller.validatePayment("12345", "invalidReference", "100.00"), "Expected reference not found, but returned true.");
    }

    @Test
    @DisplayName("Amount Mismatch")
    void testAmountMismatch() {
        assertFalse(controller.validatePayment("12345", "validReference", "50.00"), "Expected amount mismatch, but returned true.");
    }

    @Test
    @DisplayName("Incorrect Input Types")
    void testIncorrectInputTypes() {
        assertFalse(controller.validatePayment("1", "2", "3"), "Expected incorrect input types, but returned true.");
    }

    @Test
    @DisplayName("Not Null HashMap")
    void testNotNullHashMap() {
        assertNotNull(controller.getHashMap(), "Expected non-null HashMap, but received null.");
    }

    @Test
    @DisplayName("Valid Input")
    void testValidInput() {
        assertTrue(controller.validateInput("12345", "123456789", "100.00"), "Expected valid input, but returned false.");
    }

    @Test
    @DisplayName("Invalid Reference Length")
    void testInvalidReferenceLength() {
        assertFalse(controller.validateInput("12345", "1234", "123"), "Expected invalid reference length, but returned true.");
    }

    @Test
    @DisplayName("Invalid Amount Format")
    void testInvalidAmountFormat() {
        assertFalse(controller.validateInput("12345", "123456789", "invalid"), "Expected invalid amount format, but returned true.");
    }
}
