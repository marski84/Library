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
CREATE INDEX IF NOT EXISTS idx_user_id ON users (id);
-----
CREATE TABLE IF NOT EXISTS books
(
    id        BIGSERIAL PRIMARY KEY,
    title     VARCHAR(255) NOT NULL,
    author    VARCHAR(255) NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    isbn      VARCHAR(50)  NULL,
    pages     INTEGER      NOT NULL,
    CONSTRAINT uq_books_isbn UNIQUE (isbn),
    CONSTRAINT chk_books_pages CHECK (CAST(pages AS INTEGER) > 0)
);

CREATE INDEX IF NOT EXISTS idx_books_isbn ON books (isbn);
CREATE INDEX IF NOT EXISTS idx_book_id ON books (id);


----
CREATE TABLE IF NOT EXISTS rentals
(
    id             BIGSERIAL PRIMARY KEY,
    book_id        BIGINT NOT NULL,
    user_id        BIGINT NOT NULL,
    rent_date      DATE   NOT NULL,
    due_date       DATE   NOT NULL,
    return_date    DATE,
    penalty_points INT DEFAULT 0,
    CONSTRAINT fk_book
        FOREIGN KEY (book_id)
            REFERENCES books (id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

CREATE INDEX IF NOT EXISTS idx_rental_id ON rentals (id);
CREATE INDEX IF NOT EXISTS idx_rentals_book_id ON rentals (book_id);
CREATE INDEX IF NOT EXISTS idx_rentals_user_id ON rentals (user_id);
CREATE INDEX IF NOT EXISTS idx_rentals_return_date ON rentals (return_date);

ALTER TABLE rentals
    ADD CONSTRAINT check_due_date
        CHECK (due_date > rent_date);

CREATE INDEX idx_active_rental ON rentals (book_id, return_date);

-----
-- CREATE TABLE library_config
-- (
--     config_key   VARCHAR(50) PRIMARY KEY,
--     config_value INTEGER NOT NULL,
--     description  VARCHAR(255),
--     last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );
-- INSERT INTO library_config (config_key, config_value, description)
-- VALUES ('max_penalty_points', 10, 'Maksymalna liczba punktów karnych dla użytkownika'),
--        ('max_user_rentals', 3, 'Maksymalna liczba wypożyczeń nsa użytkownika'),
--        ('rental_period_days', 14, 'Standardowy okres wypożyczenia w dniach'),
--        ('late_return_fee', 0.50, 'Opłata za każdy dzień spóźnienia (w PLN)');
--
-- -- Dodanie indeksu dla szybszego wyszukiwania (opcjonalne, ale zalecane dla większych tabel)
-- CREATE INDEX IF NOT EXISTS idx_library_config_key ON library_config (config_key);
