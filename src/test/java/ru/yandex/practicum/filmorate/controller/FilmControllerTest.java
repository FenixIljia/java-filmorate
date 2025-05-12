package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private Film validFilm;
    private Film anotherFilm;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        validFilm = Film.builder()
                .name("Valid Film")
                .description("Valid Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(Duration.ofMinutes(120))
                .build();

        anotherFilm = Film.builder()
                .name("Another Film")
                .description("Another Description")
                .releaseDate(LocalDate.of(2010, 5, 15))
                .duration(Duration.ofMinutes(90))
                .build();
    }

    @Test
    void shouldCreateFilmWithValidData() {
        Film created = filmController.create(validFilm);

        assertNotNull(created.getId());
        assertEquals(1L, created.getId());
        assertEquals("Valid Film", created.getName());
    }

    @Test
    void shouldThrowExceptionForEarlyReleaseDate() {
        Film earlyFilm = validFilm;
        earlyFilm.setReleaseDate(LocalDate.of(1895, 12, 27));

        assertThrows(ValidationException.class, () -> filmController.create(earlyFilm));
    }

    @Test
    void shouldAllowExactMinReleaseDate() {
        Film minDateFilm = validFilm;
        minDateFilm.setReleaseDate(LocalDate.of(1895, 12, 28));

        Film created = filmController.create(minDateFilm);
        assertEquals(LocalDate.of(1895, 12, 28), created.getReleaseDate());
    }

    @Test
    void shouldThrowExceptionForEmptyName() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Film film = validFilm;
        film.setName(" ");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldThrowExceptionForLongDescription() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        String longDescription = "a".repeat(201);
        Film longDescFilm = validFilm;
        longDescFilm.setDescription(longDescription);

        Set<ConstraintViolation<Film>> violations = validator.validate(longDescFilm);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldAllowMaxDescriptionLength() {
        String maxDescription = "a".repeat(200);
        Film maxDescFilm = validFilm;
        maxDescFilm.setDescription(maxDescription);

        Film created = filmController.create(maxDescFilm);
        assertEquals(maxDescription, created.getDescription());
    }

    @Test
    void shouldThrowExceptionForNonPositiveDuration() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Film zeroDurationFilm = validFilm;
        zeroDurationFilm.setDuration(Duration.ZERO);

        Set<ConstraintViolation<Film>> violations = validator.validate(zeroDurationFilm);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldUpdateFilmSuccessfully() {
        Film created = filmController.create(validFilm);
        Film updated = Film.builder()
                .id(created.getId())
                .name("Updated Name")
                .description("Updated Description")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(Duration.ofMinutes(150))
                .build();

        Film result = filmController.update(updated);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(LocalDate.of(2020, 10, 10), result.getReleaseDate());
        assertEquals(Duration.ofMinutes(150), result.getDuration());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingFilm() {
        Film nonExisting = validFilm;
        nonExisting.setId(999L);
        assertThrows(NotFoundException.class, () -> filmController.update(nonExisting));
    }

    @Test
    void shouldPartialUpdateFilm() {
        Film created = filmController.create(validFilm);
        Film partialUpdate = Film.builder()
                .id(created.getId())
                .name("New Name Only")
                .build();

        Film updated = filmController.update(partialUpdate);
        assertEquals("New Name Only", updated.getName());
        assertEquals(created.getDescription(), updated.getDescription());
        assertEquals(created.getReleaseDate(), updated.getReleaseDate());
        assertEquals(created.getDuration(), updated.getDuration());
    }

    @Test
    void shouldReturnAllFilms() {
        filmController.create(validFilm);
        filmController.create(anotherFilm);

        assertEquals(2, filmController.findAll().size());
    }

    @Test
    void shouldGenerateSequentialIds() {
        Film first = filmController.create(validFilm);
        Film second = filmController.create(anotherFilm);

        assertEquals(1L, first.getId());
        assertEquals(2L, second.getId());
    }

    @Test
    void shouldUpdateReleaseDateValidationOnUpdate() {
        Film invalidUpdate = filmController.create(validFilm);
        invalidUpdate.setReleaseDate(LocalDate.of(1890, 1, 1));

        assertThrows(ValidationException.class, () -> filmController.update(invalidUpdate));
    }

    @Test
    void shouldHandleMinimumDuration() {
        Film minDurationFilm = validFilm;
        minDurationFilm.setDuration(Duration.ofNanos(1));

        Film created = filmController.create(minDurationFilm);
        assertEquals(Duration.ofNanos(1), created.getDuration());
    }
}