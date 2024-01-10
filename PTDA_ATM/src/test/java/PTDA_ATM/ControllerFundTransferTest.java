package PTDA_ATM;

import PTDA_ATM.ControllerFundTransfer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ControllerFundTransferTest {

    private ControllerFundTransfer controller;

    @BeforeEach
    void setUp() {
        controller = new ControllerFundTransfer();
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }

    @Test
    @DisplayName("Valid Input: Target Account and Amount")
    void testValidInput() {
        boolean isValid = controller.validateInput("12345678901234567890", "100.00");
        assertTrue(isValid, "Expected valid input, but returned false.");
    }

    @Test
    @DisplayName("Invalid Input: Invalid Target Account Length")
    void testInvalidTargetAccountLength() {
        boolean isValid = controller.validateInput("123", "100.00");
        assertFalse(isValid, "Expected invalid target account length, but returned true.");
    }

    @Test
    @DisplayName("Valid Input: Valid Amount Format")
    void testValidAmountFormat() {
        boolean isValid = controller.validateInput("12345678901234567890", "100.00");
        assertTrue(isValid, "Expected valid amount format, but returned false.");
    }

    @Test
    @DisplayName("Invalid Input: Invalid Amount Format")
    void testInvalidAmountFormat() {
        boolean isValid = controller.validateInput("12345678901234567890", "invalid");
        assertFalse(isValid, "Expected invalid amount format, but returned true.");
    }
}
