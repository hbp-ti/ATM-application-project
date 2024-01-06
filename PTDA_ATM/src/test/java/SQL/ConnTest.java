package SQL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class ConnTest {
    private Conn conn = new Conn();;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        if (conn.isConnected()) {
            conn.close();
        }
    }

    @Test
    @DisplayName("Test to establish connection")
    void testDoConnection() {
        conn.doConnection();
        assertTrue(conn.isConnected(), "The connection should be established.");
    }

    @Test
    @DisplayName("Test to get the connection")
    void testGetConnection() {
        Connection connection = Conn.getConnection();
        assertNotNull(connection, "The connection should not be null.");
    }

    @Test
    @DisplayName("Test to close the connection")
    void testClose() {
        conn.doConnection();
        conn.close();
        assertFalse(conn.isConnected(), "The connection should be closed.");
    }

    @Test
    @DisplayName("Test to check if connected")
    void testIsConnected() {
        assertFalse(conn.isConnected(), "Initially, the connection should not be established.");

        conn.doConnection();
        assertTrue(conn.isConnected(), "The connection should be established.");

        conn.close();
        assertFalse(conn.isConnected(), "The connection should be closed.");
    }

    @Test
    @DisplayName("Test for multiple connection instances")
    void testMultipleConnectionInstances() {
        Conn conn1 = new Conn();
        conn1.doConnection();

        Conn conn2 = new Conn();
        conn2.doConnection();

        assertTrue(conn1.isConnected(), "The first connection should be established.");
        assertFalse(conn2.isConnected(), "The second connection should not be established.");

        conn1.close();
        assertFalse(conn1.isConnected(), "The first connection should be closed.");
    }
}