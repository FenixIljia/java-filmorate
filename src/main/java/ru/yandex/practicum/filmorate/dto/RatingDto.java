package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Rating;

@Data
public class RatingDto {
    String name;
    long id;
}
