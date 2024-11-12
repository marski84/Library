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