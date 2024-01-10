package PTDA_ATM;

import PTDA_ATM.ControllerMenuPayment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerMenuPaymentTest {

    private ControllerMenuPayment controller;

    @BeforeEach
    public void setUp() {
        controller = new ControllerMenuPayment();
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }

    @Test
    @DisplayName("Valid value test for setClientName")
    public void testSetValidClientName() {
        String clientName = "John Doe";
        controller.setClientName(clientName);
        assertEquals(clientName, controller.getClientName(), "Failed to assign a valid name to the client");
    }

    @Test
    @DisplayName("Null value test for setClientName")
    public void testSetNullClientName() {
        String clientName = null;
        controller.setClientName(clientName);
        assertNull(controller.getClientName(), "Failed to handle a null value for the client name");
    }

    @Test
    @DisplayName("Client name update test")
    public void testUpdateClientName() {
        String initialName = "John Doe";
        String updatedName = "Jane Smith";

        controller.setClientName(initialName);
        assertEquals(initialName, controller.getClientName(), "Failed to set the initial client name");

        controller.setClientName(updatedName);
        assertEquals(updatedName, controller.getClientName(), "Failed to update the client name");
    }

}
