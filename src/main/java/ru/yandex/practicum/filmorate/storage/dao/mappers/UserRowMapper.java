package ru.yandex.practicum.filmorate.storage.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class UserRowMapper  implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setName(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        Timestamp date = resultSet.getTimestamp("birthday");
        user.setBirthday(date.toLocalDateTime().toLocalDate());
        return user;
    }
}
