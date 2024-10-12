-- create table,add constraints and validation
CREATE TABLE IF NOT EXISTS users
(
    id             BIGSERIAL PRIMARY KEY,
    user_name      VARCHAR(255)          NOT NULL,
    first_name     VARCHAR(255)          NOT NULL,
    last_name      VARCHAR(255)          NOT NULL,
    age            INTEGER               NOT NULL,
    penalty_points INTEGER DEFAULT 0     NOT NULL,
    is_blocked     BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT uq_users_user_name UNIQUE (user_name),
    CONSTRAINT chk_penalty_points_non_negative CHECK (penalty_points >= 0)
);

CREATE INDEX IF NOT EXISTS idx_users_user_name ON users (user_name);
-----

CREATE TABLE IF NOT EXISTS books
(
    id        BIGSERIAL PRIMARY KEY,
    title     VARCHAR(255) NOT NULL,
    author    VARCHAR(255) NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    isbn      VARCHAR(255) NOT NULL,
    pages     VARCHAR(255) NOT NULL,
    user_id   BIGINT,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT uq_books_isbn UNIQUE (isbn),
    CONSTRAINT chk_books_pages CHECK (CAST(pages AS INTEGER) > 0)
);

CREATE INDEX IF NOT EXISTS idx_books_isbn ON books (isbn);

----


