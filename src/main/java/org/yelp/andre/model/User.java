package org.yelp.andre.model;

import lombok.Data;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.yelp.andre.utility.TableUtils.getSqlErrorMessage;

@Data
@Getter
public class User {
    private String userId;
    private String name;
    private int reviewCount;
    private Timestamp yelpingSince;
    private int useful;
    private int funny;
    private int cool;
    private int fans;
    private double avgStars;

    public User (ResultSet rs) {
        try {
            userId = rs.getString("user_id");
            name = rs.getString("name");
            reviewCount = rs.getInt("review_count");
            yelpingSince = rs.getTimestamp("yelping_since");
            useful = rs.getInt("useful");
            funny = rs.getInt("funny");
            cool = rs.getInt("cool");
            fans = rs.getInt("fans");
            avgStars = rs.getDouble("average_stars");
        } catch (SQLException e) {
            System.err.println(getSqlErrorMessage(e));
        }
    }
}
