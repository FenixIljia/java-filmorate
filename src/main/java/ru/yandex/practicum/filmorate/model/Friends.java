package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friends {
    private final long userId;
    private final long friendId;
    private final String status; // PENDING, CONFIRMED
}