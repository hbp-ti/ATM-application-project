package PTDA_ATM;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    @DisplayName("Validate Payment: Valid Payment")
    public void testValidatePayment_ValidPayment_ReturnsTrue() {
        // Mock a valid payment entity, reference, and amount that exist in the payment map
        boolean isValid = controller.validatePayment("12345", "123456789", "123.12");
        assertTrue(isValid, "Expected a valid payment, but returned false.");
    }

    @Test
    @DisplayName("Validate Payment: Reference Not Found")
    public void testValidatePayment_ReferenceNotFound_ReturnsFalse() {
        // Use a payment reference that does not exist in the payment map
        boolean isValid = controller.validatePayment("12345", "invalidReference", "100.00");
        assertFalse(isValid, "Expected a reference not found, but returned true.");
    }

    @Test
    @DisplayName("Validate Payment: Amount Mismatch")
    public void testValidatePayment_AmountMismatch_ReturnsFalse() {
        // Use a payment reference with an amount that doesn't match the stored amount for that reference
        boolean isValid = controller.validatePayment("12345", "validReference", "50.00");
        assertFalse(isValid, "Expected an amount mismatch, but returned true.");
    }

    @Test
    @DisplayName("Validate Payment: Incorrect Input Types")
    public void testValidatePayment_IncorrectInputTypes_ReturnsFalse() {
        // Pass non-numeric entity, reference, or amount
        boolean isValid = controller.validatePayment(".", ".", ".");
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
        // Pass valid entity, reference, and amount
        boolean isValid = controller.validateInput("12345", "123456789", "100.00");
        assertTrue(isValid, "Expected valid input, but returned false.");
    }

    @Test
    @DisplayName("Validate Input: Invalid Reference Length")
    public void testValidateInput_InvalidReferenceLength_ReturnsFalse() {
        // Pass a reference that doesn’t have 9 digits
        boolean isValid = controller.validateInput("12345", "1234", "123");
        assertFalse(isValid, "Expected invalid reference length, but returned true.");
    }

    @Test
    @DisplayName("Validate Input: Invalid Amount Format")
    public void testValidateInput_InvalidAmountFormat_ReturnsFalse() {
        // Pass an amount in an incorrect format (non-numeric or invalid decimal format)
        boolean isValid = controller.validateInput("12345", "123456789", "invalid");
        assertFalse(isValid, "Expected invalid amount format, but returned true.");
    }
}
