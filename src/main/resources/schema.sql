
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR NOT NULL,
    login VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    birthday TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS rating (
    rating_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR,
    description VARCHAR
);

CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_name VARCHAR NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date TIMESTAMP NOT NULL,
    duration BIGINT NOT NULL,
    rating_id BIGINT REFERENCES rating(rating_id)
);

CREATE TABLE IF NOT EXISTS genre (
    genre_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    genre_name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id BIGINT REFERENCES films(film_id),
    genre_id BIGINT REFERENCES genre(genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id),
    film_id BIGINT REFERENCES films(film_id)
);

CREATE TABLE IF NOT EXISTS friendship (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id),
    friend_id BIGINT REFERENCES users(user_id),
    status BOOLEAN
);

CREATE TABLE IF NOT EXISTS reviews (
    id IDENTITY NOT NULL PRIMARY KEY,
    film_id BIGINT,
    user_id BIGINT,
    content VARCHAR,
    positiv BOOLEAN,
    FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT unique_keys_reviews UNIQUE(film_id, user_id)
);

CREATE TABLE IF NOT EXISTS reviews_likes (
    id IDENTITY NOT NULL PRIMARY KEY,
    review_id BIGINT,
    user_id BIGINT,
    like_value INTEGER,
    FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT unique_keys_reviews_likes UNIQUE(review_id, user_id)
);
