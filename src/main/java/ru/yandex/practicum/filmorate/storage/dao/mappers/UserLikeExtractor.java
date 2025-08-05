package ru.yandex.practicum.filmorate.storage.dao.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Component
public class UserLikeExtractor implements ResultSetExtractor<Map<User, Set<Long>>> {
    @Override
    public Map<User, Set<Long>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<User, Set<Long>> userLikes = new HashMap<>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setName(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setBirthday(rs.getTimestamp("birthday").toLocalDateTime().toLocalDate());
            userLikes.putIfAbsent(user, new LinkedHashSet<>());
            if (rs.getInt("film_id") != 0) {
                userLikes.get(user).add(rs.getLong("film_id"));
            }
        }
        return userLikes;
    }
}
