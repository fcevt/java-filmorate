
MERGE INTO genre USING
(VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик')) AS src (genre_name)
ON genre.genre_name = src.genre_name
WHEN MATCHED THEN
    UPDATE SET genre_name = src.genre_name
WHEN NOT MATCHED THEN
INSERT (genre_name) VALUES (src.genre_name)
;

MERGE INTO rating USING
    (VALUES ('G'),
            ('PG'),
            ('PG-13'),
            ('R'),
            ('NC-17')) AS src (code)
    ON rating.code = src.code
    WHEN MATCHED THEN
        UPDATE SET code = src.code
    WHEN NOT MATCHED THEN
        INSERT (code) VALUES (src.code);