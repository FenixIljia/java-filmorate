package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmForPostmanTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmForPostmanTestMapper {
    public static FilmForPostmanTest mapToFilmDto (Film film) {
        FilmForPostmanTest dto = new FilmForPostmanTest();
        dto.setId(film.getId());
        dto.setDescription(film.getDescription());
        dto.setName(film.getName());
        dto.setDuration(film.getDuration().toSeconds());
        dto.setReleaseDate(film.getReleaseDate());
        dto.addGenres(film.getGenres().getFirst().getId());
        dto.setMpa(film.getRating().getId());
        return dto;
    }
}
