package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {

    private final FilmStorage storage;

    private final FilmService service;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен список всех фильмов.");
        return storage.findAll();
    }

    @GetMapping("/popular")
    public Set<Film> getPopularFilmForLike(@RequestParam(defaultValue = "10") long count) {
        return service.getPopularFilmForLike(count);
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        return storage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        return storage.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long userId) {
        return service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable long id, @PathVariable long userId) {
        return service.removeLike(id, userId);
    }
}
