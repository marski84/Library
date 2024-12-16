DROP TABLE IF EXISTS rentals;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

CREATE TABLE books (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       isbn VARCHAR(255) NOT NULL,
                       pages INT,
                       publisher VARCHAR(255)
);


CREATE TABLE users
(
    id             SERIAL PRIMARY KEY,
    user_name      VARCHAR(255)          NOT NULL,
    first_name     VARCHAR(255)          NOT NULL,
    last_name      VARCHAR(255)          NOT NULL,
    age            INTEGER               NOT NULL,
    penalty_points INTEGER DEFAULT 0     NOT NULL,
    is_blocked     BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT uq_users_user_name UNIQUE (user_name),
    CONSTRAINT chk_penalty_points_non_negative CHECK (penalty_points >= 0)
    );


CREATE TABLE IF NOT EXISTS rentals
(
    id             SERIAL PRIMARY KEY,
    book_id BIGINT,
    user_id BIGINT,
    rent_date      DATE   NOT NULL,
    due_date       DATE   NOT NULL,
    return_date    DATE,
    penalty_points INT DEFAULT 0,
    CONSTRAINT fk_book
        FOREIGN KEY (book_id)
            REFERENCES books (id)
            ON DELETE SET NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE SET NULL
);