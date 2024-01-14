package org.yelp.andre.controller;

import lombok.NonNull;
import org.yelp.andre.model.SearchableTable;
import org.yelp.andre.model.User;

import java.sql.*;
import java.util.*;

import static com.microsoft.sqlserver.jdbc.StringUtils.EMPTY;
import static java.util.Objects.isNull;
import static org.yelp.andre.utility.TableUtils.*;

public class UserController extends SearchableTable {
    private final Scanner scanner;

    public UserController(Connection connection, Scanner scanner) {
        super(connection, scanner);
        this.scanner = getScanner();
    }

    @Override
    @NonNull
    public String getResultSetInfo(ResultSet rs) throws SQLException {
        return rs.getString("user_id") + ", " + rs.getString("name") + ", " +
                rs.getInt("review_count") + ", " + rs.getInt("useful") + ", " +
                rs.getInt("funny") + ", " + rs.getInt("cool") + ", " +
                rs.getDouble("average_stars") + ", " + rs.getTimestamp("yelping_since");
    }

    @Override
    public String getSearchOrdering() {
        return " ORDER BY name";
    }

    @Override
    public User search(String id) {
        String query = "SELECT * FROM User_yelp WHERE user_id like '" + id + "'";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return new User(rs);
            }
        } catch (SQLException e) {
            System.err.println(getSqlErrorMessage(e));
        }
        return null;
    }

    @Override
    public User searchAndValidate(String id) {
        User user = search(id);
        while (isNull(user)) {
            System.out.println("\nError! No user found with ID: " + id);
            System.out.print("Please enter a valid user ID: ");
            id = scanner.next();
            user = search(id);
        }
        return user;
    }

    private String filterByName() {
        System.out.println("\nFilter Users by Name");
        System.out.print("Enter the name: ");
        String name = scanner.next();
        return "name like '%" + name + "%'";
    }

    private String filterByReviewCount() {
        System.out.println("\nFilter Businesses by Review Count");
        int reviews = getIntInput(scanner, "Enter the number of reviews: ");
        return "review_count >= " + reviews;
    }

    private String filterByAvgStars() {
        System.out.println("\nFilter Users by Average Stars");
        double stars = getDoubleInputWithinRange(scanner, "Enter the number of average stars (0.0 - 5.0): ", 0, 5);
        return "average_stars >= " + stars;
    }

    @Override
    public String searchAll() {
        System.out.println("\nSearch Users\n");
        System.out.println("Filter by:");
        System.out.println("(1) Name");
        System.out.println("(2) Minimum Review Count");
        System.out.println("(3) Minimum Average Stars");
        System.out.println("(0) Return to menu\n");

        String options = getOptionsInput("Enter the options you would like to filter by, separated by spaces (ex: 1 2 3): ");
        List<Integer> optionList = getOptionList(options);

        if (optionList.contains(0)) {
            return EMPTY;
        }
        StringBuilder query = new StringBuilder("SELECT * FROM user_yelp WHERE ");
        for (int i = 0; i < optionList.size(); i++) {
            int option = optionList.get(i);
            switch (option) {
                case 1 -> query.append(filterByName());
                case 2 -> query.append(filterByReviewCount());
                case 3 -> query.append(filterByAvgStars());
                default -> System.out.println("Skipping option number: " + option);
            }
            if (i < optionList.size()-1) {
                query.append(" AND ");
            }
        }
        return query.append(getSearchOrdering()).toString();
    }

    private boolean isFriends(User user, User friend) {
        String query = "SELECT * FROM friendship WHERE " +
                "(user_id like '" + user.getUserId() + "' AND friend like '" + friend.getUserId() +"') OR " +
                "(user_id like '" + friend.getUserId() + "' AND friend like '" + user.getUserId() +"') ";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println(getSqlErrorMessage(e));
        }
        return false;
    }

    public void addFriend(User user) {
        System.out.println("\nAdd a Friend\n");
        System.out.print("Send friend request to user ID ('x' to return to menu): ");
        String friendId = scanner.next();

        if (friendId.equalsIgnoreCase("x")) {
            System.out.println("Returning to menu...");
            return;
        }
        User friend = searchAndValidate(friendId);
        if (isFriends(user, friend)) {
            System.out.println("User is already friends with friend: " + friend.getName());
            return;
        }
        String query = "INSERT INTO Friendship (user_id, friend) VALUES (?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, friend.getUserId());
            pstmt.executeUpdate();
            System.out.println("Successfully added friend: " + friend.getName());
        } catch (SQLException e) {
            System.err.println(getSqlErrorMessage(e));
        }
    }
}
