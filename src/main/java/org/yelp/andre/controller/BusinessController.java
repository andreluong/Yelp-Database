package org.yelp.andre.controller;

import lombok.NonNull;
import org.yelp.andre.model.SearchableTable;
import org.yelp.andre.model.Business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.microsoft.sqlserver.jdbc.StringUtils.EMPTY;
import static java.util.Objects.isNull;
import static org.yelp.andre.utility.TableUtils.*;

public class BusinessController extends SearchableTable {
    private final Scanner scanner;

    public BusinessController(Connection connection, Scanner scanner) {
        super(connection, scanner);
        this.scanner = getScanner();
    }

    @Override
    @NonNull
    public String getResultSetInfo(ResultSet rs) throws SQLException {
        return rs.getString("business_id") + ", " + rs.getString("name") + ", " +
                rs.getString("address") + ", " + rs.getString("city") + ", " +
                rs.getString("stars");
    }

    @Override
    public String getSearchOrdering() {
        final String ORDER_BY = " ORDER BY ";
        System.out.println("\nOrder Businesses by:");
        System.out.println("(1) Name");
        System.out.println("(2) City");
        System.out.println("(3) Number of stars\n");

        int orderInput = getIntInputWithinRange(scanner, "Enter a number from 1-3: ", 1, 3);

        switch (orderInput) {
            case 1 -> {
                return ORDER_BY + "name";
            }
            case 2 -> {
                return ORDER_BY + "city";
            }
            case 3 -> {
                return ORDER_BY + "stars";
            }
            default -> {
                return EMPTY;
            }
        }
    }

    @Override
    public Business search(String id) {
        String query = "SELECT * FROM Business WHERE business_id = '" + id + "'";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return new Business(rs);
            }
        } catch (SQLException e) {
            System.err.println(getSqlErrorMessage(e));
        }
        return null;
    }

    @Override
    public Business searchAndValidate(String id) {
        Business business = search(id);
        while (isNull(business)) {
            System.out.println("\nError! No business found with ID: " + id);
            System.out.print("Please enter a valid business ID: ");
            id = scanner.next();
            business = search(id);
        }
        return business;
    }

    private String filterByStars() {
        System.out.println("\nFilter Businesses by Stars");
        double stars = getDoubleInputWithinRange(scanner, "Enter the minimum number of stars (0.0 - 5.0): ", 0, 5);
        return "stars >= " + stars;
    }

    private String filterByCity() {
        System.out.println("\nFilter Businesses by City");
        System.out.print("Enter the City name: ");
        String city = scanner.next();
        return "city = '" + city + "'";
    }

    private String filterByName() {
        System.out.println("\nFilter Businesses by Name");
        System.out.print("Enter the Business name: ");
        String name = scanner.next();
        return "name like '%" + name + "%'";
    }

    @Override
    public String searchAll() {
        System.out.println("\nSearch Businesses\n");
        System.out.println("Filter by:");
        System.out.println("(1) Minimum Number of Stars");
        System.out.println("(2) City");
        System.out.println("(3) Name");
        System.out.println("(0) Return to menu\n");

        String options = getOptionsInput("Enter the options you would like to filter by, separated by spaces (ex: 1 2 3): ");
        List<Integer> optionList = getOptionList(options);

        if (optionList.contains(0)) {
            return EMPTY;
        }

        StringBuilder query = new StringBuilder("SELECT * FROM business WHERE ");
        for (int i = 0; i < optionList.size(); i++) {
            int option = optionList.get(i);
            switch (option) {
                case 1 -> query.append(filterByStars());
                case 2 -> query.append(filterByCity());
                case 3 -> query.append(filterByName());
                default -> System.out.println("Skipping option number: " + option);
            }
            if (i < optionList.size()-1) {
                query.append(" AND ");
            }
        }
        return query.append(getSearchOrdering()).toString();
    }
}