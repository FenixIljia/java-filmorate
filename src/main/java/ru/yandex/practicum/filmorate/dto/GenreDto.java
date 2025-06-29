package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;

@Data
public class GenreDto {
    private long genre_id;
    private String name;
}
