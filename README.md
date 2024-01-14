# Yelp Database
A CLI Yelp database manager. Features include searching businesses and users, adding friends, and reviewing a business.
- Searching for a business provides up to 3 options (business name, city, and minimum number of stars) to filter by and 3 types (business name, city, and stars) to order by.
- Searching for a user provides up to 3 options (name, review count, and average stars) to filter by and are ordered by name.

## Setup
1. Refer to `resources/database/setup.sql` for my database setup. Then `BULK INSERT` the data from the CSV files into the tables.
2. `Gradle build`
3. Define the environment variables in `Run/Debug configurations` for `Application/Main` (You may have to run the application first to get the option)
   - Database server - `DB_SERVER`
   - Username - `DB_USERNAME`
   - Password for user - `DB_PASSWORD`

## Instructions
1. Login with a valid user id (Any user_id from user_yelp.csv).
2. Use any options from the provided menu.