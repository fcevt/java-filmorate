package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Event {
    long eventId;
    long timestamp;
    long userId;
    String eventType;
    String operation;
    long entityId;
}
