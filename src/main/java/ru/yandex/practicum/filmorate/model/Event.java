package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Event {
    long eventId;
    Timestamp timestamp;
    long userId;
    String evenType;
    String operation;
    long entityId;
}
