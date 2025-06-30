package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.FilmForPostmanTest;
import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmForPostmanTestMapper {
    public static FilmForPostmanTest mapToFilmDto (Film film) {
        FilmForPostmanTest dto = new FilmForPostmanTest();
        dto.setId(film.getId());
        dto.setDescription(film.getDescription());
        dto.setName(film.getName());
        dto.setDuration(film.getDuration().getSeconds());
        dto.setReleaseDate(film.getReleaseDate());
        dto.addGenres(film.getGenres().getFirst().getId());
        dto.setMpa(film.getRating().getId());
        return dto;
    }
}
