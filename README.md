# java-filmorate
Template repository for Filmorate project.
![диаграмма бд](https://github.com/fcevt/java-filmorate/blob/add-database/ER%20diagram.jpg)
Примеры обращения к БД  
- получение всех пользователей
```sql
SELECT *
FROM user;
``` 
- получение всех фильмов
```sql
SELECT *
FROM film;
```
- получение пользователя по id = 5 например 
```sql
SELECT *
FROM user
WHERE id = 5;
```
- получение списка друзей пользователя с id = 5
```sql
SELECT *
FROM user
WHERE user_id IN (SELECT friend_id
                 FROM friendship
                 WHERE user_id = 5);
```
- получение списка общих друзей у пользователей с id = 5 и id = 6
```sql
SELECT *
FROM user
WHERE user_id IN (SELECT friend_id
                  FROM friendship
                  WHERE user_id = 6
                        AND friend_id IN (SELECT friend_id
                                          FROM friendship
                                          WHERE user_id = 5));
```
- получение 10 самых популярных фильмов
```sql
SELECT *
FROM film
WHERE film_id IN (SELECT film_id
                  FROM likes
                  GROUP BY film_id
                  ORDER BY COUNT(user_id) DESC
                  LIMIT 10);
```
