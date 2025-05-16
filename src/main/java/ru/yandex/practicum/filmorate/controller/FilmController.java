package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм - {}.", film.getName());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        if (film.getReleaseDate() != null) {
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.warn(
                        "Попытка установить недопустимую дату релизу фильма(" +
                                "дата релиза фильма должна быть не раньше 28 декабря 1895 года)." +
                                " Дата релиза фильма - {}.",
                        film.getReleaseDate()
                );
                throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
            }
        }
        if (!films.containsKey(film.getId())) {
            log.warn("Попытка обновить фильм, которого нет в базе. id фильм - ${}.", film.getId());
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }

        Film oldFilm = films.get(film.getId());

        if (film.getReleaseDate() != null) {
            oldFilm.setReleaseDate(film.getReleaseDate());
            log.debug("Дата релиза фильма {} обновлена на: {}.", oldFilm.getName(), oldFilm.getReleaseDate());
        }
        if (film.getDescription() != null) {
            oldFilm.setDescription(film.getDescription());
            log.debug("Описание фильма {} обновлено на: {}.", oldFilm.getName(), oldFilm.getDescription());
        }
        if (film.getDuration() != null) {
            oldFilm.setDuration(film.getDuration());
            log.debug("Продолжительность фильма {} обновлена на: {}.", oldFilm.getName(), oldFilm.getDuration());
        }
        if (film.getName() != null) {
            log.debug("Название фильма {} изменено на: {}.", oldFilm.getName(), film.getName());
            oldFilm.setName(film.getName());
        }
        log.info("Фильма с id ${} успешно обновлен", oldFilm.getId());
        return oldFilm;
    }

    //Генерация id
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
