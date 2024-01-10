package PTDA_ATM;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ControllerDepositTest {
    private ControllerDeposit controller;

    @BeforeEach
    void setUp() {
        controller = new ControllerDeposit();
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }

    @Test
    @DisplayName("Valid Deposit Amount")
    void testValidDepositAmount() {
        boolean isValid = controller.validateInput("100.00");
        assertTrue(isValid, "Expected valid input, but returned false.");
    }

    @Test
    @DisplayName("Invalid Deposit Amount: Non-Numeric")
    void testInvalidDepositAmount_NonNumeric() {
        boolean isValid = controller.validateInput("abc");
        assertFalse(isValid, "Expected invalid amount (non-numeric), but returned true.");
    }

    @Test
    @DisplayName("Invalid Deposit Amount: Invalid Format")
    void testInvalidDepositAmount_InvalidFormat() {
        boolean isValid = controller.validateInput("1.2.3");
        assertFalse(isValid, "Expected invalid amount (invalid format), but returned true.");
    }

    @Test
    @DisplayName("Invalid Deposit Amount: No Decimal Part")
    void testInvalidDepositAmount_NoDecimalPart() {
        boolean isValid = controller.validateInput("100");
        assertTrue(isValid, "Expected valid input (integer), but returned false.");
    }

    @Test
    @DisplayName("Invalid Deposit Amount: Negative Value")
    void testInvalidDepositAmount_NegativeValue() {
        boolean isValid = controller.validateInput("-100.00");
        assertFalse(isValid, "Expected invalid input (negative value), but returned true.");
    }
}
