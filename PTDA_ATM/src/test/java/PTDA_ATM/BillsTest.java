package PTDA_ATM;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BillsTest {
    private Bills bills;

    @BeforeEach
    void setUp() {
        bills = new Bills();
    }

    @Test
    @DisplayName("NotNull: Get Payment Map")
    void testGetPayment_NotNull() {
        HashMap<String, Object> payment = bills.getPayment();
        assertNotNull(payment, "Map of accounts and services should not be null.");
    }

    @Test
    @DisplayName("Map Size: Get Payment Map")
    void testGetPayment_MapSize() {
        HashMap<String, Object> payment = bills.getPayment();
        int expectedSize = 16;
        assertEquals(expectedSize, payment.size(), "Map size should match the expected size.");
    }

    @Test
    @DisplayName("InstanceOf: Accounts and Services")
    void testBillsObject_InstanceOfServicesOrTheState() {
        HashMap<String, Object> payment = bills.getPayment();

        for (Object value : payment.values()) {
            assertTrue(value instanceof Services || value instanceof TheState, "Each value should be an instance of Services or TheState.");
        }
    }
}
