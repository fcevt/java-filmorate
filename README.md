# java-filmorate
Template repository for Filmorate project.
![диаграмма бд](https://github.com/fcevt/java-filmorate/blob/add-database/ER%20diagram.jpg)
Примеры обращения к БД  
- получение всех пользователей
```
SELECT *
FROM user;
``` 
- получение всех фильмов
```
SELECT *
FROM film;
```
- получение пользователя по id = 5 например 
```
SELECT *
FROM user
WHERE id = 5;
```
- получение списка друзей пользователя с id = 5
```
SELECT *
FROM user
WHERE user_id IN (SELECT friend_id
                 FROM friendship
                 WHERE user_id = 5);
```
- получение списка общих друзей у пользователей с id = 5 и id = 6
```
SELECT *
FROM user
WHERE user_id IN (SELECT friend_id
                  FROM friendship
                  WHERE user_id = 6
                        AND friend_id IN (SELECT friend_id
                                          FROM friendship
                                          WHERE user_id = 5));
```
