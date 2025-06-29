package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.dto.GenreDto;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
@AllArgsConstructor
public class GenreController {

    private final GenreDbStorage storage;

    @GetMapping
    public List<GenreDto> findAll() {
        return storage.findAll();
    }

    @GetMapping("/{id}")
    public GenreDto findById(@PathVariable long id) {
        return storage.findById(id).get();
    }
}
