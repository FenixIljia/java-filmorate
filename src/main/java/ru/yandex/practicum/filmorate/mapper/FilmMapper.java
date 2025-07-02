package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmForUpdate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setDescription(film.getDescription());
        dto.setName(film.getName());
        dto.setDuration(film.getDuration().toMinutes());
        dto.setReleaseDate(film.getReleaseDate());
        for (Long l : film.getLikeUser()) {
            dto.addLike(l);
        }
        dto.setMpa(film.getRating());
        dto.setGenres(film.getGenres());
        return dto;
    }

    public static FilmDto mapToFilmDto(FilmForUpdate film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setDescription(film.getDescription());
        dto.setName(film.getName());
        dto.setDuration(film.getDuration());
        dto.setReleaseDate(film.getReleaseDate());
        return dto;
    }

    public static Film updateFilmField(Film film, FilmForUpdate request) {
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
        if (request.getDuration() == 0) {
            film.setDuration(Duration.ofMinutes(request.getDuration()));
        }
        if (request.getRating() != null) {
            film.setRating(request.getRating());
        }
        return film;
    }
}
