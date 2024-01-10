package PTDA_ATM;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    @DisplayName("Validate Payment: Valid Payment")
    public void testValidatePayment_ValidPayment_ReturnsTrue() {
        // Mock a valid payment reference and amount that exist in the payment map
        boolean isValid = controller.validatePayment("123456789012345", "124.12");
        assertTrue(isValid, "Expected a valid payment, but returned false.");
    }

    @Test
    @DisplayName("Validate Payment: Reference Not Found")
    public void testValidatePayment_ReferenceNotFound_ReturnsFalse() {
        // Use a payment reference that does not exist in the payment map
        boolean isValid = controller.validatePayment("invalidReference", "100.00");
        assertFalse(isValid, "Expected a reference not found, but returned true.");
    }

    @Test
    @DisplayName("Validate Payment: Amount Mismatch")
    public void testValidatePayment_AmountMismatch_ReturnsFalse() {
        // Use a payment reference with an amount that doesn't match the stored amount for that reference
        boolean isValid = controller.validatePayment("validReference", "50.00");
        assertFalse(isValid, "Expected an amount mismatch, but returned true.");
    }

    @Test
    @DisplayName("Validate Payment: Incorrect Input Types")
    public void testValidatePayment_IncorrectInputTypes_ReturnsFalse() {
        // Pass a non-numeric reference or amount
        boolean isValid = controller.validatePayment(".", ".");
        assertFalse(isValid, "Expected incorrect input types, but returned true.");
    }

    @Test
    @DisplayName("Get Payment Map: Not Null HashMap")
    public void testGetHashMap_NotNullHashMap_ReturnsNotNull() {
        HashMap<String, Object> paymentMap = controller.getHashMap();
        assertNotNull(paymentMap, "Expected a non-null HashMap, but received null.");
    }
    @Test
    @DisplayName("Validate Input: Valid Input")
    public void testValidateInput_ValidInput_ReturnsTrue() {
        // Pass a valid 15-digit numeric reference and a valid amount
        boolean isValid = controller.validateInput("123456789012345", "100.00");
        assertTrue(isValid, "Expected valid input, but returned false.");
    }

    @Test
    @DisplayName("Validate Input: Invalid Reference Length")
    public void testValidateInput_InvalidReferenceLength_ReturnsFalse() {
        // Pass a reference that doesn’t have 15 digits
        boolean isValid = controller.validateInput("1234", "100.00");
        assertFalse(isValid, "Expected invalid reference length, but returned true.");
    }

    @Test
    @DisplayName("Validate Input: Invalid Amount Format")
    public void testValidateInput_InvalidAmountFormat_ReturnsFalse() {
        // Pass an amount in an incorrect format (non-numeric or invalid decimal format)
        boolean isValid = controller.validateInput("123456789012345", "invalid");
        assertFalse(isValid, "Expected invalid amount format, but returned true.");
    }
}