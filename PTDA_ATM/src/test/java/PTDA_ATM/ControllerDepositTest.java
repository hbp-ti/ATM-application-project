package PTDA_ATM;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    public void testValidDepositAmount() {
        boolean isValid = controller.validateInput("100.00");
        assertTrue(isValid, "Expected valid input, but returned false.");
    }

    @Test
    @DisplayName("Invalid Deposit Amount: Non-Numeric")
    public void testInvalidDepositAmount_NonNumeric() {
        ControllerDeposit controller = new ControllerDeposit();
        boolean isValid = controller.validateInput("abc");
        assertFalse(isValid, "Expected invalid deposit amount (non-numeric), but returned true.");
    }

    @Test
    @DisplayName("Invalid Deposit Amount: Invalid Format")
    public void testInvalidDepositAmount_InvalidFormat() {
        ControllerDeposit controller = new ControllerDeposit();
        boolean isValid = controller.validateInput("1.2.3");
        assertFalse(isValid, "Expected invalid deposit amount (invalid format), but returned true.");
    }

    @Test
    @DisplayName("Invalid Deposit Amount: No Decimal Part")
    public void testInvalidDepositAmount_NoDecimalPart() {
        ControllerDeposit controller = new ControllerDeposit();
        boolean isValid = controller.validateInput("100");
        assertTrue(isValid, "Expected valid input (integer), but returned false.");
    }

    @Test
    @DisplayName("Invalid Deposit Amount: Negative Value")
    public void testInvalidDepositAmount_NegativeValue() {
        ControllerDeposit controller = new ControllerDeposit();
        boolean isValid = controller.validateInput("-100.00");
        assertFalse(isValid, "Expected invalid input (negative value), but returned true.");
    }
}
