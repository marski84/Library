INSERT INTO books (id, title, author, isbn, pages, publisher)
VALUES
    (1, 'Clean Code', 'Robert C. Martin', '978-0132350884',  464, 'Prentice Hall'),
    (2, 'Design Patterns', 'Erich Gamma', '978-0201633610', 416, 'Addison-Wesley'),
    (3, 'Effective Java', 'Joshua Bloch', '978-0134685991',  412, 'Addison-Wesley'),
    (4, 'Domain-Driven Design', 'Eric Evans', '978-0321125217',  560, 'Addison-Wesley');

ALTER TABLE books ALTER COLUMN id RESTART WITH 5;


INSERT INTO users (id, user_name, first_name, last_name, age, penalty_points, is_blocked)
VALUES
    (1, 'jkowalski', 'Jan', 'Kowalski', 35, 2, false),
    (2, 'anowak', 'Anna', 'Nowak', 28, 8, false),
    (3, 'pmalinowski', 'Piotr', 'Malinowski', 45, 12, true),
    (4, 'mwisniewski', 'Marek', 'Wiśniewski', 22, 0, false);

ALTER TABLE users ALTER COLUMN id RESTART WITH 5;
