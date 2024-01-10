package PTDA_ATM;

import PTDA_ATM.ControllerFundTransfer;
import PTDA_ATM.ControllerServicePayment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    @DisplayName("Validate Input: Valid Target Account and Amount")
    public void testValidateInput_ValidTargetAccountAndAmount_ReturnsTrue() {
        boolean isValid = controller.validateInput("12345678901234567890", "100.00");
        assertTrue(isValid, "Expected valid input, but returned false.");
    }

    @Test
    @DisplayName("Validate Input: Invalid Target Account Length")
    public void testValidateInput_InvalidTargetAccountLength_ReturnsFalse() {
        boolean isValid = controller.validateInput("123", "100.00");
        assertFalse(isValid, "Expected invalid target account length, but returned true.");
    }

    @Test
    @DisplayName("Validate Input: Valid Amount Format")
    public void testValidateInput_ValidAmountFormat_ReturnsTrue() {
        boolean isValid = controller.validateInput("12345678901234567890", "100.00");
        assertTrue(isValid, "Expected valid amount format, but returned false.");
    }

    @Test
    @DisplayName("Validate Input: Invalid Amount Format")
    public void testValidateInput_InvalidAmountFormat_ReturnsFalse() {
        boolean isValid = controller.validateInput("12345678901234567890", "invalid");
        assertFalse(isValid, "Expected invalid amount format, but returned true.");
    }
}
