package org.yelp.andre.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(null, null);
    }

    @Test
    void getResultSetInfo() throws SQLException {
        ResultSet mockRs = mock(ResultSet.class);
        when(mockRs.getString("user_id")).thenReturn("om5ZiponkpRqUNa3pVPiRg");
        when(mockRs.getString("name")).thenReturn("Andrea");
        when(mockRs.getInt("review_count")).thenReturn(2811);
        when(mockRs.getInt("useful")).thenReturn(18545);
        when(mockRs.getInt("funny")).thenReturn(9225);
        when(mockRs.getInt("cool")).thenReturn(13824);
        when(mockRs.getDouble("average_stars")).thenReturn(3.97);
        when(mockRs.getTimestamp("yelping_since")).thenReturn(Timestamp.valueOf("2006-01-18 02:35:04"));

        String expected = "om5ZiponkpRqUNa3pVPiRg, Andrea, 2811, 18545, 9225, 13824, 3.97, 2006-01-18 02:35:04.0";
        assertEquals(expected, userController.getResultSetInfo(mockRs));
    }

    @Test
    void getSearchOrdering() {
        assertEquals(" ORDER BY name", userController.getSearchOrdering());
    }
}