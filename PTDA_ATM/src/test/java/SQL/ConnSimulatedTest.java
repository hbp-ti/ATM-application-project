package SQL;

import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class ConnSimulatedTest {

    private ConnSimulated conn;

    @BeforeEach
    void setUp() {
        conn = new ConnSimulated();
    }

    @AfterEach
    void tearDown() {
        conn = null;
    }

    @Test
    @DisplayName("Test Successful Connection")
    void testSuccessfulConnection() {
        conn.doConnection();
        assertTrue(conn.isConnected(), "The connection should be established successfully.");
    }

    @Test
    @DisplayName("Test Connection Closure")
    void testCloseConnection() {
        conn.doConnection();
        conn.close();
        assertFalse(conn.isConnected(), "The connection should be closed.");
    }

    @Test
    @DisplayName("Test Connection Failure")
    void testConnectionFailure() {
        conn.setSimulateConnectionFailure(true);
        conn.doConnection();
        assertFalse(conn.isConnected(), "The connection should fail.");
    }

    @Test
    @DisplayName("Test Successful Connection After Failure")
    void testSuccessfulConnectionAfterFailure() {
        conn.setSimulateConnectionFailure(true);
        conn.doConnection();
        conn.setSimulateConnectionFailure(false);
        conn.doConnection();
        assertTrue(conn.isConnected(), "The connection should be successful after a previous failure.");
    }

    @Test
    @DisplayName("Test Initial Connection State")
    void testInitialConnectionState() {
        assertFalse(conn.isConnected(), "Initially, the connection should not be established.");
    }

    @Test
    @DisplayName("Test Retrieving Simulated Connection")
    void testGetSimulatedConnection() {
        conn.setSimulateConnectionFailure(false); // Ensure the connection will be successful
        Connection fictitiousConnection = ConnSimulated.getSimConnection();
        assertNotNull(fictitiousConnection, "Should get a simulated connection.");
        assertEquals(fictitiousConnection.getClass(), conn.getSimConnection().getClass(), "Should get the same simulated connection.");
    }

    @Test
    @DisplayName("Test Retrieving Simulated Connection After Failure")
    void testGetSimulatedConnectionAfterFailure() {
        conn.setSimulateConnectionFailure(true); // Simulate a connection failure
        assertNull(ConnSimulated.getSimConnection(), "Should not get connection after a simulated failure.");
    }
}
