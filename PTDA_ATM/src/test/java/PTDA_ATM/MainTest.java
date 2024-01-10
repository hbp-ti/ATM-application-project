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

    @Test
    @DisplayName("Get Connection")
    public void testGetConnection() {
        main.setConnection(conn);
        assertEquals(conn, main.getConnection(), "Should get the connection set");
    }

    @Test
    @DisplayName("Set Connection")
    public void testSetConnection() {
        main.setConnection(conn);
        assertEquals(conn, main.getConnection(), "Should set the connection");
    }

    @Test
    @DisplayName("Stop with Open Connection")
    public void testStopWithOpenConnection() {
        main.setConnection(conn);
        main.stop();
        assertFalse(conn.isConnected(), "Connection should be closed after stopping the application");
    }

    @Test
    @DisplayName("Stop with Closed Connection")
    public void testStopWithClosedConnection() {
        main.setConnection(conn);
        conn.doConnection();
        conn.close();
        main.stop();
        assertFalse(conn.isConnected(), "Connection should remain closed after stopping the application");
    }
}
