-- Tables
CREATE TABLE user_yelp
(
    user_id CHAR(22) PRIMARY KEY,
    name VARCHAR(35) NOT NULL,
    review_count INTEGER DEFAULT 0 CHECK (review_count >= 0),
    yelping_since DATETIME DEFAULT GETDATE() NOT NULL,
    useful INTEGER DEFAULT 0 CHECK (useful >= 0),
    funny INTEGER DEFAULT 0 CHECK (funny >= 0),
    cool INTEGER DEFAULT 0 CHECK (cool >= 0),
    fans INTEGER DEFAULT 0 CHECK (fans >= 0),
    average_stars DECIMAL (3,2) CHECK (average_stars >= 1 AND average_stars <=5)
)

CREATE TABLE business
(
    business_id CHAR(22) PRIMARY KEY,
    name VARCHAR(60) NOT NULL,
    address VARCHAR(75),
    city VARCHAR(30) NOT NULL,
    postal_code VARCHAR(7),
    stars DECIMAL(2,1) CHECK (stars >= 1 AND stars <=5),
    review_count INTEGER DEFAULT 0 CHECK (review_count >= 0)
)

CREATE TABLE checkin
(
    checkin_id INTEGER PRIMARY KEY,
    business_id CHAR(22) NOT NULL FOREIGN KEY REFERENCES Business(business_id),
    date DATETIME DEFAULT GETDATE() NOT NULL
)

CREATE TABLE tip
(
    tip_id INTEGER PRIMARY KEY,
    user_id CHAR(22) NOT NULL FOREIGN KEY REFERENCES user_yelp(user_id),
    business_id CHAR(22) NOT NULL FOREIGN KEY REFERENCES Business(business_id),
    date DATETIME DEFAULT GETDATE() NOT NULL,
    compliment_count INTEGER DEFAULT 0 CHECK (compliment_count  >= 0)
)

CREATE TABLE friendship
(
    user_id CHAR(22),
    friend CHAR(22),
    PRIMARY KEY (user_id, friend),
    FOREIGN KEY (user_id) REFERENCES user_yelp(user_id),
    FOREIGN KEY (friend) REFERENCES user_yelp(user_id),
)

CREATE TABLE review
(
    review_id CHAR(22) PRIMARY KEY,
    user_id CHAR(22) NOT NULL FOREIGN KEY REFERENCES user_yelp(user_id),
    business_id CHAR(22) NOT NULL FOREIGN KEY REFERENCES Business(business_id),
    stars INTEGER NOT NULL CHECK (stars >= 1 AND stars <=5),
    useful INTEGER DEFAULT 0 CHECK (useful >= 0),
    funny INTEGER DEFAULT 0 CHECK (funny >= 0),
    cool INTEGER DEFAULT 0 CHECK (cool >= 0),
    date DATETIME DEFAULT GETDATE()
)

-- Trigger
CREATE TRIGGER updateReviewsBusiness
ON review
AFTER INSERT
AS BEGIN
    update business
    set review_count = (
        select count(*)
        from review
        where (
            select max(date) max_date
            from review r
            where r.user_id = review.user_id and business_id = inserted.business_id
            group by user_id) = review.date
        ),
    stars = (
        select AVG(CAST(review.stars AS DECIMAL(2,1)))
        from review
        where (
            select max(date) max_date
            from review r
            where r.user_id = review.user_id and business_id = inserted.business_id
            group by user_id) = review.date
        )
    from business, inserted
    where business.business_id = inserted.business_id
END;