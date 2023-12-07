package org.yelp.andre.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TableUtils {
    private static final String INVALID_NUM_ERROR_MSG = "Error! Invalid number input found.\n ";
    public static final String UNIMPLEMENTED_METHOD_ERROR_MSG = "Unimplemented method found for: ";

    public static int getIntInput(Scanner scanner, String askMessage) {
        System.out.print(askMessage);
        while (!scanner.hasNextInt()) {
            System.out.print(INVALID_NUM_ERROR_MSG + askMessage);
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static int getIntInputWithinRange(Scanner scanner, String askMessage, int min, int max) {
        int num = getIntInput(scanner, askMessage);
        while (num < min || num > max) {
            System.out.print(INVALID_NUM_ERROR_MSG);
            num = getIntInput(scanner, askMessage);
        }
        return num;
    }

    public static double getDoubleInput(Scanner scanner, String askMessage) {
        System.out.print(askMessage);
        while (!scanner.hasNextDouble()) {
            System.out.print(INVALID_NUM_ERROR_MSG + askMessage);
            scanner.next();
        }
        return scanner.nextDouble();
    }

    public static double getDoubleInputWithinRange(Scanner scanner, String askMessage, int min, int max) {
        double num = getDoubleInput(scanner, askMessage);
        while (num < min || num > max) {
            System.out.print(INVALID_NUM_ERROR_MSG);
            num = getDoubleInput(scanner, askMessage);
        }
        return num;
    }

    public static String getSqlErrorMessage(SQLException e) {
        return "\nSQL Exception occurred:\n" +
                "state : " + e.getSQLState() +
                "\nMessage: " + e.getMessage() + "\n";
    }
}
