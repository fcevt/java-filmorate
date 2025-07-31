package ru.yandex.practicum.filmorate.storage.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewMapper implements RowMapper<Review> {
    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
//        Long id = rs.getLong("id");
//        Long film_id = rs.getLong("film_id");
//        Long user_id = rs.getLong("user_id");
//        String content = rs.getString("content");
//        Short useful = rs.getShort("useful");

//                .user(rs.getLong("user_id"))
//                .content(rs.getString("content"))
//                .isPositive(rs.getBoolean("positiv"))
//                //.useful(rs.getByte("useful"))
//                .build();

        return Review.builder()
                .id(rs.getLong("id"))
                .film(rs.getLong("film_id"))
                .user(rs.getLong("user_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("positiv"))
                .useful(rs.getShort("useful"))
                .build();
    }
}
