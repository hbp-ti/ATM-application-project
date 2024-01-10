package PTDA_ATM;

import PTDA_ATM.ControllerMenuPayment;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerMenuPaymentTest {

    private ControllerMenuPayment controller;

    @BeforeEach
    void setUp() {
        controller = new ControllerMenuPayment();
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }

    @Test
    @DisplayName("Set Valid Client Name")
    void testSetValidClientName() {
        String clientName = "John Doe";
        controller.setClientName(clientName);
        assertEquals(clientName, controller.getClientName(), "Failed to assign a valid name to the client");
    }

    @Test
    @DisplayName("Set Null Client Name")
    void testSetNullClientName() {
        String clientName = null;
        controller.setClientName(clientName);
        assertNull(controller.getClientName(), "Failed to handle a null value for the client name");
    }

    @Test
    @DisplayName("Update Client Name")
    void testUpdateClientName() {
        String initialName = "John Doe";
        String updatedName = "Jane Smith";

        controller.setClientName(initialName);
        assertEquals(initialName, controller.getClientName(), "Failed to set the initial client name");

        controller.setClientName(updatedName);
        assertEquals(updatedName, controller.getClientName(), "Failed to update the client name");
    }
}
