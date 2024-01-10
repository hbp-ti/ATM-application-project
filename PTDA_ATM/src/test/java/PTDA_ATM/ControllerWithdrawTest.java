package PTDA_ATM;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerWithdrawTest {
    private ControllerWithdraw controller;
    @BeforeEach
    void setUp() {
        controller = new ControllerWithdraw();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testValidWithdrawAmount() {
        assertTrue(controller.validateInput("50.25"));
    }

    @Test
    @DisplayName("Valid Withdraw Amount")
    public void testValidWithdrawAmountt() {
        assertTrue(controller.validateInput("50.25"), "Expected valid withdrawal amount");
    }

    @Test
    @DisplayName("Non-Numeric Withdraw Amount")
    public void testNonNumericWithdrawAmount() {
        assertFalse(controller.validateInput("abc"), "Expected non-numeric input to be invalid");
    }

    @Test
    @DisplayName("Negative Withdraw Amount")
    public void testNegativeWithdrawAmount() {
        assertFalse(controller.validateInput("-30.75"), "Expected negative input to be invalid");
    }

    @Test
    @DisplayName("Invalid Format Withdraw Amount")
    public void testInvalidFormatWithdrawAmount() {
        assertFalse(controller.validateInput("10,50"), "Expected invalid format to be invalid");
    }

    @Test
    @DisplayName("Multiple Decimal Points Withdraw Amount")
    public void testMultipleDecimalPointsWithdrawAmount() {
        assertFalse(controller.validateInput("25.5.75"), "Expected multiple decimal points to be invalid");
    }

    @Test
    @DisplayName("Whitespace Withdraw Amount")
    public void testWhitespaceWithdrawAmount() {
        assertFalse(controller.validateInput(" 30.50 "), "Expected whitespace input to be invalid");
    }

    @Test
    @DisplayName("Empty Withdraw Amount")
    public void testEmptyWithdrawAmount() {
        assertFalse(controller.validateInput(""), "Expected empty input to be invalid");
    }


}