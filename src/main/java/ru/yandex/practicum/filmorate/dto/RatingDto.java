package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Rating;

@Data
public class RatingDto {
    Rating name;
    long rating_id;
}
