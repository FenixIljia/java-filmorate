package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class LikeUser {
    long user_id;
    long film_id;
}
