package org.yelp.andre.model;

import lombok.Data;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.yelp.andre.utility.TableUtils.getSqlErrorMessage;

@Data
@Getter
public class Business {
    private String businessId;
    private String name;
    private String address;
    private String city;
    private String postalCode;
    private double stars;
    private int reviewCount;

    public Business(ResultSet rs) {
        try {
            businessId = rs.getString("business_id");
            name = rs.getString("name");
            address = rs.getString("address");
            city = rs.getString("city");
            postalCode = rs.getString("postal_code");
            stars = rs.getDouble("stars");
            reviewCount = rs.getInt("review_count");
        } catch (SQLException e) {
            System.err.println(getSqlErrorMessage(e));
        }
    }
}
