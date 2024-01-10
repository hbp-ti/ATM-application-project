package PTDA_ATM;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerChargePhoneTest {
    private ControllerChargePhone controller;
    @BeforeEach
    void setUp() {
        controller = new ControllerChargePhone();
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }

    @Test
    @DisplayName("Valid Input")
    public void testValidInput() {
        boolean isValid = controller.validateInput("123456789", "100.00");
        assertTrue(isValid, "Expected valid input, but returned false.");
    }

    @Test
    @DisplayName("Invalid Phone Number: More Than 9 Digits")
    public void testInvalidPhoneNumber_MoreThanNineDigits() {
        boolean isValid = controller.validateInput("1234567890", "100.00");
        assertFalse(isValid, "Expected invalid phone number (more than 9 digits), but returned true.");
    }

    @Test
    @DisplayName("Invalid Phone Number: Non-Numeric")
    public void testInvalidPhoneNumber_NonNumeric() {
        boolean isValid = controller.validateInput("abc", "100.00");
        assertFalse(isValid, "Expected invalid phone number (non-numeric), but returned true.");
    }

    @Test
    @DisplayName("Invalid Amount: Non-Numeric")
    public void testInvalidAmount_NonNumeric() {
        boolean isValid = controller.validateInput("123456789", "abc");
        assertFalse(isValid, "Expected invalid amount (non-numeric), but returned true.");
    }

    @Test
    @DisplayName("Invalid Amount: Invalid Format")
    public void testInvalidAmount_InvalidFormat() {
        boolean isValid = controller.validateInput("123456789", "1.2.3");
        assertFalse(isValid, "Expected invalid amount (invalid format), but returned true.");
    }
}