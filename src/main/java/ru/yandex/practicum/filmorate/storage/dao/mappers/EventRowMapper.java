package ru.yandex.practicum.filmorate.storage.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EventRowMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Event event = new Event();
        event.setEventId(resultSet.getLong("event_id"));
        event.setTimestamp(resultSet.getTimestamp("timestamp"));
        event.setUserId(resultSet.getLong("user_id"));
        event.setEvenType(resultSet.getString("event_type"));
        event.setOperation(resultSet.getString("operation"));
        event.setEntityId(resultSet.getLong("entity_id"));
        return event;
    }
}
