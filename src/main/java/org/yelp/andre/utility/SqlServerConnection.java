package org.yelp.andre.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqlServerConnection {
    public static Connection getConnection(String server, String username, String passphrase) {
        try {
            System.out.println("Connecting to SQL Server...");
            Connection con = DriverManager.getConnection(server, username, passphrase);
            System.out.println("Successfully connected to SQL Server.");
            return con;
        } catch (SQLException e) {
            System.err.println ( "\n\nFailed to connect to SQL Server; exit now.\n\n" );
            e.printStackTrace(System.err);
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
        }
        throw new RuntimeException("Connection can not be established.");
    }
}
