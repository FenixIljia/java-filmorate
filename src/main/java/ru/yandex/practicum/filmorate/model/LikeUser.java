package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class LikeUser {
    long userId;
    long filmId;
}
