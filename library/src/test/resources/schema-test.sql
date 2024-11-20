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
ALTER TABLE books ALTER COLUMN id RESTART WITH 5;
