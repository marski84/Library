-- Inserty dla tabeli
INSERT INTO users (user_name, first_name, last_name, age, penalty_points, is_blocked)
VALUES ('jkowalski', 'Jan', 'Kowalski', 25, 0, false),
       ('anowak', 'Anna', 'Nowak', 32, 3, false),
       ('pwiśniewski', 'Piotr', 'Wiśniewski', 45, 8, true),
       ('mdąbrowski', 'Marek', 'Dąbrowski', 28, 2, false),
       ('klewandowska', 'Katarzyna', 'Lewandowska', 35, 0, false),
       ('azielińska', 'Agnieszka', 'Zielińska', 41, 5, false),
       ('twójcik', 'Tomasz', 'Wójcik', 29, 10, true),
       ('mkamińska', 'Maria', 'Kamińska', 38, 1, false);

-- Inserty dla tabeli books
INSERT INTO books (title, author, publisher, isbn, pages)
VALUES ('Władca Pierścieni: Drużyna Pierścienia', 'J.R.R. Tolkien', 'Wydawnictwo Amber', '978-83-241-3544-4', 448),
       ('Harry Potter i Kamień Filozoficzny', 'J.K. Rowling', 'Media Rodzina', '978-83-724-8700-1', 328),
       ('1984', 'George Orwell', 'Wydawnictwo Muza', '978-83-287-0167-3', 296),
       ('Zbrodnia i kara', 'Fiodor Dostojewski', 'Wydawnictwo MG', '978-83-779-1921-5', 589),
       ('Pan Tadeusz', 'Adam Mickiewicz', 'Greg', '978-83-756-1234-5', 376),
       ('Duma i uprzedzenie', 'Jane Austen', 'Wydawnictwo Znak', '978-83-240-2957-8', 392),
       ('Mały Książę', 'Antoine de Saint-Exupéry', 'Wydawnictwo Muza', '978-83-287-0809-2', 112),
       ('Wiedźmin: Ostatnie życzenie', 'Andrzej Sapkowski', 'SuperNowa', '978-83-375-1651-4', 332),
       ('Rok 1984', 'George Orwell', 'Wydawnictwo Muza', '978-83-287-0152-9', 312),
       ('Mistrz i Małgorzata', 'Michaił Bułhakow', 'Wydawnictwo MG', '978-83-779-1456-2', 536),
       ('Lalka', 'Bolesław Prus', 'Greg', '978-83-756-5678-9', 784),
       ('Hobbit', 'J.R.R. Tolkien', 'Wydawnictwo Amber', '978-83-241-3545-1', 320),
       ('Sherlock Holmes: Studium w szkarłacie', 'Arthur Conan Doyle', 'Wydawnictwo MG', '978-83-779-1789-1', 196),
       ('Dziady', 'Adam Mickiewicz', 'Greg', '978-83-756-9012-3', 336),
       ('Proces', 'Franz Kafka', 'Wydawnictwo MG', '978-83-779-1234-5', 280),
       ('Quo Vadis', 'Henryk Sienkiewicz', 'Greg', '978-83-756-3456-7', 648),
       ('Romeo i Julia', 'William Shakespeare', 'Wydawnictwo MG', '978-83-779-1567-8', 264),
       ('Chłopi', 'Władysław Reymont', 'Greg', '978-83-756-7890-1', 976),
       ('Trylogia: Ogniem i mieczem', 'Henryk Sienkiewicz', 'Wydawnictwo Znak', '978-83-240-1234-5', 784),
       ('Wesele', 'Stanisław Wyspiański', 'Wydawnictwo MG', '978-83-779-1890-2', 248);



-- RENTALS----------------------------------------------------------------------------------------------------------------
-- Inserty dla tabeli rentals
INSERT INTO rentals (book_id, user_id, rent_date, due_date, return_date, penalty_points_for_due)
VALUES
    -- Aktualne wypożyczenia (bez daty zwrotu)
    (1, 1, '2024-03-01 10:00:00+01', '2024-03-15 10:00:00+01', NULL, 0),
    (3, 2, '2024-03-05 14:30:00+01', '2024-03-19 14:30:00+01', NULL, 0),
    (5, 4, '2024-03-10 11:15:00+01', '2024-03-24 11:15:00+01', NULL, 0),

    -- Zwrócone książki na czas
    (2, 1, '2024-02-01 09:00:00+01', '2024-02-15 09:00:00+01', '2024-02-14 16:30:00+01', 0),
    (4, 5, '2024-02-05 13:45:00+01', '2024-02-19 13:45:00+01', '2024-02-18 12:00:00+01', 0),
    (6, 8, '2024-02-10 15:20:00+01', '2024-02-24 15:20:00+01', '2024-02-23 17:00:00+01', 0),

    -- Zwrócone książki po terminie (z naliczonymi punktami karnymi)
    (7, 2, '2024-01-15 10:00:00+01', '2024-01-29 10:00:00+01', '2024-02-05 14:30:00+01', 3),
    (8, 6, '2024-01-20 11:30:00+01', '2024-02-03 11:30:00+01', '2024-02-10 09:15:00+01', 5),
    (9, 3, '2024-01-25 16:45:00+01', '2024-02-08 16:45:00+01', '2024-02-15 13:00:00+01', 8),

    -- Historia wypożyczeń z poprzednich miesięcy
    (10, 4, '2023-12-01 09:30:00+01', '2023-12-15 09:30:00+01', '2023-12-14 15:45:00+01', 0),
    (11, 7, '2023-12-05 14:15:00+01', '2023-12-19 14:15:00+01', '2023-12-20 11:30:00+01', 1),
    (12, 8, '2023-12-10 10:45:00+01', '2023-12-24 10:45:00+01', '2023-12-23 16:00:00+01', 0),
    (13, 1, '2023-12-15 13:00:00+01', '2023-12-29 13:00:00+01', '2023-12-28 14:30:00+01', 0),
    (14, 5, '2023-12-20 15:30:00+01', '2024-01-03 15:30:00+01', '2024-01-02 12:15:00+01', 0),

    -- Wypożyczenia z dużym opóźnieniem
    (15, 3, '2023-11-01 10:00:00+01', '2023-11-15 10:00:00+01', '2023-12-01 09:00:00+01', 10),
    (16, 6, '2023-11-05 11:45:00+01', '2023-11-19 11:45:00+01', '2023-12-05 14:30:00+01', 8),
    (17, 7, '2023-11-10 14:20:00+01', '2023-11-24 14:20:00+01', '2023-12-10 16:45:00+01', 9),

    -- Najnowsze wypożyczenia
    (18, 2, '2024-03-12 09:15:00+01', '2024-03-26 09:15:00+01', NULL, 0),
    (19, 4, '2024-03-13 13:30:00+01', '2024-03-27 13:30:00+01', NULL, 0),
    (20, 8, '2024-03-14 15:45:00+01', '2024-03-28 15:45:00+01', NULL, 0);