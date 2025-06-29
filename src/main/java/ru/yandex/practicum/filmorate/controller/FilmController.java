package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmForPostmanTest;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmForUpdate;
import ru.yandex.practicum.filmorate.model.LikeUser;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {

    private final FilmService service;

    @GetMapping
    public Collection<FilmDto> findAll() {
        log.info("Получен список всех фильмов.");
        return service.findAll();
    }

    @GetMapping("/{id}")
    public FilmDto find(@PathVariable long id) {
        return service.find(id);
    }

    @GetMapping("/popular")
    public List<FilmDto> getPopularFilmForLike(@RequestParam(defaultValue = "10") long count) {
        return service.getPopularFilmForLike(count);
    }

    @PostMapping
    public FilmDto create(@RequestBody @Valid Film film) {
        return service.create(film);
    }

    @PutMapping
    public FilmDto update(@RequestBody @Valid FilmForUpdate film) {
        return service.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public LikeUser addLike(@PathVariable long id, @PathVariable long userId) {
        return service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        service.removeLike(id, userId);
    }
}
