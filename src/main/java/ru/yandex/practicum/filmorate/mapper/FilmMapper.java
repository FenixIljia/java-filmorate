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
import ru.yandex.practicum.filmorate.model.FilmForUpdate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    public static FilmDto mapToFilmDto (Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setDescription(film.getDescription());
        dto.setName(film.getName());
        dto.setDuration(film.getDuration().toSeconds());
        dto.setReleaseDate(film.getReleaseDate());
        for (Long l : film.getLikeUser()) {
            dto.addLike(l);
        }
        return dto;
    }

    public static FilmDto mapToFilmDto (FilmForUpdate film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setDescription(film.getDescription());
        dto.setName(film.getName());
        dto.setDuration(film.getDuration().toSeconds());
        dto.setReleaseDate(film.getReleaseDate());
        return dto;
    }

    public static Film updateFilmField (Film film, FilmForUpdate request) {
        if (!request.getGenres().isEmpty()) {
            for (Genre genre : request.getGenres()) {
                film.addGenre(genre);
            }
        }
        if (!request.getLikeUser().isEmpty()) {
            for (User user : request.getLikeUser()) {
                film.addLike(user.getId());
            }
        }
        if (request.getName() != null) {
            film.setName(request.getName());
        }
        if (request.getDescription() != null) {
            film.setDescription(request.getDescription());
        }
        if (request.getReleaseDate() != null) {
            film.setReleaseDate(request.getReleaseDate());
        }
        if (request.getDuration() != null) {
            film.setDuration(request.getDuration());
        }
        if (request.getRating() != null) {
            film.setRating(request.getRating());
        }
        return film;
    }
}
