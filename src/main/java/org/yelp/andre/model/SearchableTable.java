package org.yelp.andre.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static org.yelp.andre.utility.TableUtils.getSqlErrorMessage;

@AllArgsConstructor
@Getter

public abstract class SearchableTable {
    private Connection connection;
    private Scanner scanner;

    public abstract String getResultSetInfo(ResultSet rs) throws SQLException;

    public abstract String getSearchOrdering();

    public abstract Object search(String id);

    public abstract Object searchAndValidate(String id);

    public abstract String searchAll();

    // Asks the user for 1-3 options spaced out in a line
    public String getOptionsInput(String askMessage) {
        System.out.print(askMessage);
        String input = scanner.next();

        while (!input.matches("^([0-3]||[0-3] [0-3]||[0-3] [0-3] [0-3])$")) {
            System.out.println("Error! The options must be between 0-3, separated by spaces, and a max length of 5 characters.");
            System.out.print(askMessage);
            input = scanner.next();
        }
        return input;
    }

    // Get a list of distinct integers
    public List<Integer> getOptionList(String options) {
        List<Integer> optionList = new ArrayList<>();
        for (char c : options.toCharArray()) {
            if (c != ' ') {
                int n = parseInt(String.valueOf(c));
                if (!optionList.contains(n)) {
                    optionList.add(n);
                }
            }
        }
        return optionList;
    }

    // Prints the result set of the query
    public void print(String query) {
        if (query.isEmpty()) {
            System.out.println("\nReturning to menu...\n");
            return;
        }

        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            int count = 0;
            if (!rs.isBeforeFirst()) {
                System.out.println("\nNo values found for search result.");
                return;
            }
            while (rs.next()) {
                System.out.println(getResultSetInfo(rs));
                count++;
            }
            System.out.println("Count: " + count);
            rs.close();
        } catch (SQLException e) {
            System.err.println(getSqlErrorMessage(e));
        }
    }
}
