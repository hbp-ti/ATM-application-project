package PTDA_ATM;

import PTDA_ATM.ControllerOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerOptionsTest {

    private ControllerOptions controller;

    @BeforeEach
    void setUp() {
        controller = new ControllerOptions();
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }


    @Test
    @DisplayName("Set valid account number")
    void setClientAccountNumber_ValidInput_ShouldSetAccountNumber() {
        // Arrange
        String accountNumber = "1234567890";

        // Act
        controller.setClientAccountNumber(accountNumber);

        // Assert
        assertEquals(accountNumber, controller.getClientAccountNumber(), "Account number should be set");
    }

    @Test
    @DisplayName("Set null account number")
    void setClientAccountNumber_NullInput_ShouldNotSetAccountNumber() {
        // Arrange
        String accountNumber = null;

        // Act
        controller.setClientAccountNumber(accountNumber);

        // Assert
        assertNull(controller.getClientAccountNumber(), "Account number should not be set with null input");
    }

}
