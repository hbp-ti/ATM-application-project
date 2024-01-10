package PTDA_ATM;

import SQL.Conn;
import SQL.ConnSimulated;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private Main main;
    private ConnSimulated conn;
    @BeforeEach
    public void setUp() {
        main = new Main();
        conn = new ConnSimulated();
    }

    @AfterEach
    public void tearDown() {
        main = null;
    }

    @DisplayName("Test: Get Connection")
    @Test
    public void testGetConnection() {
        main.setConnection(conn);
        assertEquals(conn, main.getConnection(), "Should get the connection set");
    }

    @DisplayName("Test: Set Connection")
    @Test
    public void testSetConnection() {
        main.setConnection(conn);
        assertEquals(conn, main.getConnection(), "Should set the connection");
    }

    @DisplayName("Test: Stop Method with Open Connection")
    @Test
    public void testStopWithOpenConnection() {
        main.setConnection(conn);
        main.stop();
        assertFalse(conn.isConnected(), "Connection should be closed after stopping the application");
    }

    @DisplayName("Test: Stop Method with Closed Connection")
    @Test
    public void testStopWithClosedConnection() {
        main.setConnection(conn);
        conn.doConnection();
        conn.close();
        main.stop();
        assertFalse(conn.isConnected(), "Connection should remain closed after stopping the application");
    }
}
