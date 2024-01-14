package org.yelp.andre.utility;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Scanner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.yelp.andre.utility.TableUtils.*;

class TableUtilsTest {
    @Test
    void getSuccessfulIntInput() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.hasNextInt()).thenReturn(true);
        when(mockScanner.nextInt()).thenReturn(1);
        assertEquals(1, getIntInput(mockScanner, "running getSuccessfulIntInput()\n"));
    }

    @Test
    void getSuccessfulIntInputWithinRange() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.hasNextInt()).thenReturn(true);
        when(mockScanner.nextInt()).thenReturn(3);
        assertEquals(3, getIntInputWithinRange(mockScanner, "running getSuccessfulIntInputWithinRange()\n", 1, 5));
    }

    @Test
    void getSuccessfulDoubleInput() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.hasNextDouble()).thenReturn(true);
        when(mockScanner.nextDouble()).thenReturn(1.4);
        assertEquals(1.4, getDoubleInput(mockScanner, "running getSuccessfulDoubleInput()\n"));
    }

    @Test
    void getSuccessfulDoubleInputWithinRange() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.hasNextDouble()).thenReturn(true);
        when(mockScanner.nextDouble()).thenReturn(3.0);
        assertEquals(3.0, getDoubleInputWithinRange(mockScanner, "running getSuccessfulIntInputWithinRange()\n", 1, 5));
    }

    @Test
    void getSqlErrorMessageTest() {
        SQLException mockException = mock(SQLException.class);
        when(mockException.getSQLState()).thenReturn("01000");
        when(mockException.getMessage()).thenReturn("message");
        assertEquals("\nSQL Exception occurred:\nstate : 01000\nMessage: message\n", getSqlErrorMessage(mockException));
    }

    @Nested
    @DisplayName("Tests after one invalid input")
    class OneInvalidInput {
        private ByteArrayOutputStream outContent;

        @BeforeEach
        void setUp() {
            outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));
        }

        @AfterEach
        void tearDown() {
            System.setOut(null);
        }

        @Test
        void getIntInputAfterInvalid() {
            Scanner mockScanner = mock(Scanner.class);
            when(mockScanner.hasNextInt()).thenReturn(false, true);
            when(mockScanner.nextInt()).thenReturn(2);

            assertEquals(2, getIntInput(mockScanner, "running getIntInputAfterInvalid()\n"));
            assertThat(outContent.toString(), containsString("Error! Invalid number input found.\n running getIntInputAfterInvalid()"));
        }

        @Test
        void getIntInputWithinRangeAfterInvalid() {
            Scanner mockScanner = mock(Scanner.class);
            when(mockScanner.hasNextInt()).thenReturn(true);
            when(mockScanner.nextInt()).thenReturn(10, 3);

            assertEquals(3, getIntInputWithinRange(mockScanner, "running getIntInputWithinRangeAfterInvalid()\n", 1, 5));
            assertThat(outContent.toString(), containsString("Error! Invalid number input found.\n running getIntInputWithinRangeAfterInvalid()"));
        }

        @Test
        void getDoubleInputAfterInvalid() {
            Scanner mockScanner = mock(Scanner.class);
            when(mockScanner.hasNextDouble()).thenReturn(false, true);
            when(mockScanner.nextDouble()).thenReturn(2.1);

            assertEquals(2.1, getDoubleInput(mockScanner, "running getDoubleInputAfterInvalid()\n"));
            assertThat(outContent.toString(), containsString("Error! Invalid number input found.\n running getDoubleInputAfterInvalid()"));
        }

        @Test
        void getDoubleInputWithinRangeAfterInvalid() {
            Scanner mockScanner = mock(Scanner.class);
            when(mockScanner.hasNextDouble()).thenReturn(true);
            when(mockScanner.nextDouble()).thenReturn(10.0, 3.2);

            assertEquals(3.2, getDoubleInputWithinRange(mockScanner, "running getDoubleInputWithinRangeAfterInvalid()\n", 1, 5));
            assertThat(outContent.toString(), containsString("Error! Invalid number input found.\n running getDoubleInputWithinRangeAfterInvalid()\n"));
        }
    }
}