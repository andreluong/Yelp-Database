package org.yelp.andre.controller;


import org.yelp.andre.model.User;

import java.sql.Connection;
import java.util.Scanner;

import static org.yelp.andre.utility.SqlServerConnection.getConnection;
import static org.yelp.andre.utility.TableUtils.getIntInput;

public class YelpDatabaseController {
    private final Scanner scanner;

    private final UserController userController;

    private final BusinessController businessController;

    private final ReviewController reviewController;

    private final User user;

    public YelpDatabaseController() {
        final Connection connection = getConnection(System.getenv("DB_SERVER"), System.getenv("DB_USERNAME"), System.getenv("DB_PASSWORD"));
        this.scanner = new Scanner(System.in).useDelimiter("\\R");
        this.userController = new UserController(connection, scanner);
        this.businessController = new BusinessController(connection, scanner);
        this.reviewController = new ReviewController(connection, scanner);
        this.user = login();
    }

    private User login() {
        System.out.println("\n-------------\nYelp Database\n-------------\n");
        System.out.print("Please login with your user ID: ");
        String loginId = scanner.next();
        User loginUser = userController.searchAndValidate(loginId);
        System.out.println("Successfully logged in. Hello " + loginUser.getName() + "!");
        return loginUser;
    }

    private void quit() {
        System.out.println("\nExiting program...");
    }

    public void run() {
        int menuInput = -1;
        while (menuInput != 0) {
            System.out.println("\n-------------\nYelp Database\n-------------\n");
            System.out.println("(1) Search Businesses");
            System.out.println("(2) Review Business");
            System.out.println("(3) Search Users");
            System.out.println("(4) Add Friend");
            System.out.println("(0) Exit");
            menuInput = getIntInput(scanner, "\nEnter a number: ");

            switch (menuInput) {
                case 0 -> quit();
                case 1 -> businessController.print(businessController.searchAll());
                case 2 -> reviewController.addReview(user, businessController);
                case 3 -> userController.print(userController.searchAll());
                case 4 -> userController.addFriend(user);
                default -> System.out.println("\nError! Invalid number option found. Please enter a number from 0 - 4.\n");
            }
        }
    }
}