package ru.yandex.practicum.filmorate.storage.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MPARowMapper implements RowMapper<MPA> {
    @Override
    public MPA mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        MPA rating = new MPA();
        rating.setId(resultSet.getInt("rating_id"));
        rating.setName(resultSet.getString("code"));
        rating.setDescription(resultSet.getString("description"));
        return rating;
    }
}
