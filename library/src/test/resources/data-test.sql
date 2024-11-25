INSERT INTO books (id, title, author, isbn, pages, publisher)
VALUES
    (1, 'Clean Code', 'Robert C. Martin', '978-0132350884',  464, 'Prentice Hall'),
    (2, 'Design Patterns', 'Erich Gamma', '978-0201633610', 416, 'Addison-Wesley'),
    (3, 'Effective Java', 'Joshua Bloch', '978-0134685991',  412, 'Addison-Wesley'),
    (4, 'Domain-Driven Design', 'Eric Evans', '978-0321125217',  560, 'Addison-Wesley'),
    (5, 'Test Driven Development', 'Kent Beck', '978-0321146533', 240, 'Addison-Wesley'),
    (6, 'Refactoring', 'Martin Fowler', '978-0134757599', 448, 'Addison-Wesley');

ALTER TABLE books ALTER COLUMN id RESTART WITH 7;


INSERT INTO users (id, user_name, first_name, last_name, age, penalty_points, is_blocked)
VALUES
    (1, 'jkowalski', 'Jan', 'Kowalski', 35, 2, false),
    (2, 'anowak', 'Anna', 'Nowak', 28, 8, false),
    (3, 'pmalinowski', 'Piotr', 'Malinowski', 45, 12, true),
    (4, 'mwisniewski', 'Marek', 'Wiśniewski', 22, 0, false);

ALTER TABLE users ALTER COLUMN id RESTART WITH 5;

INSERT INTO rentals (id, book_id, user_id, rent_date, due_date, return_date, penalty_points)
VALUES
    -- Aktywne wypożyczenia (bez return_date)
    (1, 1, 1, '2024-03-01', '2024-03-15', NULL, 0),          -- Jan Kowalski wypożyczył "Clean Code"
    (2, 2, 2, '2024-03-05', '2024-03-19', NULL, 0),          -- Anna Nowak wypożyczyła "Design Patterns"

    -- Zakończone wypożyczenia na czas
    (3, 3, 4, '2024-02-01', '2024-02-15', '2024-02-14', 0),  -- Marek Wiśniewski oddał "Effective Java" przed terminem
    (4, 4, 1, '2024-02-01', '2024-02-15', '2024-02-15', 0),  -- Jan Kowalski oddał "Domain-Driven Design" w terminie
    (5, 4, 4, '2024-03-01', '2024-04-15', '2024-04-12', 0),  -- Jan Kowalski oddał "Domain-Driven Design" w terminie

    -- Zakończone wypożyczenia po terminie (z punktami karnymi)
    (6, 1, 2, '2024-01-15', '2024-01-29', '2024-02-05', 3),  -- Anna Nowak oddała z opóźnieniem
    (7, 2, 3, '2024-01-01', '2024-01-15', '2024-02-01', 8);  -- Piotr Malinowski oddał z dużym opóźnieniem

ALTER TABLE rentals ALTER COLUMN id RESTART WITH 8;

