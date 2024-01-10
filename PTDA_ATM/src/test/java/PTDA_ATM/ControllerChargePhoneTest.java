package PTDA_ATM;

import org.junit.jupiter.api.*;
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
    void testValidInput() {
        assertTrue(controller.validateInput("123456789", "100.00"), "Expected valid input, but returned false.");
    }

    @Test
    @DisplayName("Invalid Phone Number: More Than 9 Digits")
    void testInvalidPhoneNumber_MoreThanNineDigits() {
        assertFalse(controller.validateInput("1234567890", "100.00"), "Expected invalid phone number (more than 9 digits), but returned true.");
    }

    @Test
    @DisplayName("Invalid Phone Number: Non-Numeric")
    void testInvalidPhoneNumber_NonNumeric() {
        assertFalse(controller.validateInput("abc", "100.00"), "Expected invalid phone number (non-numeric), but returned true.");
    }

    @Test
    @DisplayName("Invalid Amount: Non-Numeric")
    void testInvalidAmount_NonNumeric() {
        assertFalse(controller.validateInput("123456789", "abc"), "Expected invalid amount (non-numeric), but returned true.");
    }

    @Test
    @DisplayName("Invalid Amount: Invalid Format")
    void testInvalidAmount_InvalidFormat() {
        assertFalse(controller.validateInput("123456789", "1.2.3"), "Expected invalid amount (invalid format), but returned true.");
    }
}
