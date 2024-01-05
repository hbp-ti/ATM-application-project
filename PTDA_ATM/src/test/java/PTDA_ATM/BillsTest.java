package PTDA_ATM;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BillsTest {
    private Bills bills;

    @BeforeEach
    public void setUp() {
        bills = new Bills();
    }

    // Input partition test
    @DisplayName("Test: Get Map of Accounts and Services - Check for non-null")
    @Test
    public void testGetPayment_NotNull() {
        HashMap<String, Object> payment = bills.getPayment();
        assertNotNull(payment, "The map of accounts and services should not be null.");
    }

    // Input partition test
    @DisplayName("Test: Get Map of Accounts and Services - Check map size")
    @Test
    public void testGetPayment_MapSize() {
        HashMap<String, Object> payment = bills.getPayment();
        int expectedSize = 16;
        assertEquals(expectedSize, payment.size(), "The map size should match the expected size.");
    }

    // Structural test
    @DisplayName("Test: Available Accounts and Services - Check correct instance")
    @Test
    public void testBillsObject_InstanceOfServicesOrTheState() {
        HashMap<String, Object> payment = bills.getPayment();

        for (Object value : payment.values()) {
            assertTrue(value instanceof Services || value instanceof TheState, "Each value should be an instance of Services or TheState.");
        }
    }
}
