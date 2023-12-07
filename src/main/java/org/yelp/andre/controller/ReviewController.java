package org.yelp.andre.controller;

import org.yelp.andre.model.SearchableTable;
import org.yelp.andre.model.Business;
import org.yelp.andre.model.User;

import java.sql.*;
import java.util.Scanner;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.yelp.andre.utility.TableUtils.*;

public class ReviewController extends SearchableTable {
    private final Scanner scanner;

    public ReviewController(Connection connection, Scanner scanner) {
        super(connection, scanner);
        this.scanner = getScanner();
    }

    private boolean reviewExists(String id) {
        String query = "SELECT * FROM Review WHERE review_id = '" + id + "'";
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

    private String getValidReviewId() {
        final int REVIEW_ID_LENGTH = 22;
        String reviewId = randomAlphanumeric(REVIEW_ID_LENGTH);
        while (reviewExists(reviewId)) {
            reviewId = randomAlphanumeric(REVIEW_ID_LENGTH);
        }
        return reviewId;
    }

    public void addReview(User user, BusinessController businessController) {
        System.out.print("\nEnter business id to review: ");
        String businessId = scanner.next();
        Business business = businessController.searchAndValidate(businessId);
        addReview(user, business);
    }

    private void addReview(User user, Business business) {
        int stars = getIntInputWithinRange(scanner, "Enter the number of stars (1 - 5): ", 1, 5);
        String reviewId = getValidReviewId();

        String query = "INSERT INTO Review (review_id, user_id, business_id, stars, useful, funny, cool, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setString(1, reviewId);
            pstmt.setString(2, user.getUserId());
            pstmt.setString(3, business.getBusinessId());
            pstmt.setInt(4, stars);
            pstmt.setInt(5, 0);
            pstmt.setInt(6, 0);
            pstmt.setInt(7, 0);
            pstmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            System.out.println("\nCreating review with id: " + reviewId);
            pstmt.executeUpdate();
            System.out.println("Successfully added review for business with id: " + business.getBusinessId());
        } catch (SQLException e) {
            System.err.println(getSqlErrorMessage(e));
        }
    }

    @Override
    public String getResultSetInfo(ResultSet rs) {
        throw new UnsupportedOperationException(UNIMPLEMENTED_METHOD_ERROR_MSG + "public String getResultSetInfo(ResultSet rs)");
    }

    @Override
    public String getSearchOrdering() {
        throw new UnsupportedOperationException(UNIMPLEMENTED_METHOD_ERROR_MSG + "public String getSearchOrdering()");
    }

    @Override
    public Object search(String id) {
        throw new UnsupportedOperationException(UNIMPLEMENTED_METHOD_ERROR_MSG + "public Object search(String id)");
    }

    @Override
    public Object searchAndValidate(String id) {
        throw new UnsupportedOperationException(UNIMPLEMENTED_METHOD_ERROR_MSG + "public Object searchAndValidate(String id)");
    }

    @Override
    public String searchAll() {
        throw new UnsupportedOperationException(UNIMPLEMENTED_METHOD_ERROR_MSG + "public String searchAll()");
    }
}
