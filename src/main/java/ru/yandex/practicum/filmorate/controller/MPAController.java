package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.RatingDbStorage;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
@AllArgsConstructor
public class MPAController {

    private final RatingDbStorage storage;

    @GetMapping
    public Collection<RatingDto> findAll() {
        return storage.findAll();
    }

    @GetMapping("/{id}")
    public RatingDto findById(@PathVariable long id) {
        return storage.findById(id).get();
    }
}
