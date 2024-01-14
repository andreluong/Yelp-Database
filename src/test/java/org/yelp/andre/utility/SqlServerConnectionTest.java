package org.yelp.andre.utility;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SqlServerConnectionTest {
    // Run test when server is up
    // @Test
    void validConnectionTest() throws SQLException {
        String server = System.getenv("DB_SERVER");
        String username = System.getenv("DB_USERNAME");
        String passphrase = System.getenv("DB_PASSWORD");
        assertEquals(DriverManager.getConnection(server, username, passphrase), SqlServerConnection.getConnection(server, username, passphrase));
    }

    @Test
    void invalidConnectionTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        assertThrows(RuntimeException.class, () -> SqlServerConnection.getConnection("server", "username", "passphrase"));
        System.setOut(null);
    }
}