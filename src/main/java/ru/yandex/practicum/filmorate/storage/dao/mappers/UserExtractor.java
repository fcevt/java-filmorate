package ru.yandex.practicum.filmorate.storage.dao.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class UserExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<User, Set<Long>> users = new LinkedHashMap<>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setName(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setBirthday(rs.getTimestamp("birthday").toLocalDateTime().toLocalDate());
            users.putIfAbsent(user, new LinkedHashSet<>());
            if (rs.getInt("friend") != 0) {
                users.get(user).add(rs.getLong("friend"));
            }
        }
        return users.keySet().stream()
                .peek(user -> user.setFriends(users.get(user)))
                .toList();
    }
}
