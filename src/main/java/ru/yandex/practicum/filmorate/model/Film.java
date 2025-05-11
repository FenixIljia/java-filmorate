package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Film.
 */
@Getter
@Setter
public class Film {
    long id;
    String name;
    String description;
    LocalDateTime releaseDate;
    Duration duration;
}
