package org.yelp.andre.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.yelp.andre.utility.TableUtils.getIntInputWithinRange;

class BusinessControllerTest {
    private Scanner scanner;
    private BusinessController businessController;

    @BeforeEach
    void setUp() {
        scanner = mock(Scanner.class);
        businessController = new BusinessController(null, scanner);
    }

    @Test
    void getSuccessfulResultSetInfo() throws SQLException {
        ResultSet mockRs = mock(ResultSet.class);
        when(mockRs.getString("business_id")).thenReturn("WKMJwqnfZKsAae75RMP6jA");
        when(mockRs.getString("name")).thenReturn("Roast Coffeehouse and Wine Bar");
        when(mockRs.getString("address")).thenReturn("10359 104 Street NW");
        when(mockRs.getString("city")).thenReturn("Edmonton");
        when(mockRs.getString("stars")).thenReturn("4.0");

        String expected = "WKMJwqnfZKsAae75RMP6jA, Roast Coffeehouse and Wine Bar, 10359 104 Street NW, Edmonton, 4.0";
        assertEquals(expected, businessController.getResultSetInfo(mockRs));
    }

    @Test
    void getNullResultSetInfo() {
        assertThrows(NullPointerException.class, () -> businessController.getResultSetInfo(null));
    }

    @Test
    void getSearchOrdering() {
        when(scanner.hasNextInt()).thenReturn(true);
        when(scanner.nextInt()).thenReturn(1, 2, 3);
        when(getIntInputWithinRange(scanner, "testing getSearchOrdering()", 1, 3)).thenReturn(1,2, 3);

        assertEquals(" ORDER BY name", businessController.getSearchOrdering());
        assertEquals(" ORDER BY city", businessController.getSearchOrdering());
        assertEquals(" ORDER BY stars", businessController.getSearchOrdering());
    }
}