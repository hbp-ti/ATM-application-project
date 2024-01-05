package SQL;

import PTDA_ATM.Bills;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryTest {
    private Conn conn;
    private Query query;

    @BeforeEach
    public void setUp() {
        conn = new Conn();
        conn.doConnection();
        query = new Query();
    }

    @AfterEach
    void tearDown() {
        if (conn.isConnected()) {
            conn.close();
        }
    }

    //Input partition test
    @DisplayName("Test: Get Available Balance - Valid Account")
    @Test
    void testGetAvailableBalance_ValidAccount() {
        // Act
        float balance = query.getAvailableBalance("07913610992884713357");

        // Assert
        assertNotEquals(0.0f, balance, "Balance should be retrieved for a valid account.");
    }

    //Input partition test
    @DisplayName("Test: Get Available Balance - Invalid Account")
    @Test
    void testGetAvailableBalance_InvalidAccount() {
        // Act
        float balance = query.getAvailableBalance("07913610992884713322");

        // Assert
        assertEquals(0.0f, balance, "Balance should be 0 for an invalid account.");
    }

    // Structural test
    @DisplayName("Test: Get Available Balance - Not Null")
    @Test
    void testGetAvailableBalance_NotNull() {
        // Act
        float balance = query.getAvailableBalance("34600481445834244594");

        // Assert
        assertNotNull(balance, "Balance should not be null.");
    }

    // Structural test
    @DisplayName("Test: Get Available Balance - Zero for No Transactions")
    @Test
    void testGetAvailableBalance_ZeroForNoTransactions() {
        // Act
        float balance = query.getAvailableBalance("64254753326407403316");

        // Assert
        assertEquals(0.0f, balance, "Balance should be 0 for an account with no transactions.");
    }
}