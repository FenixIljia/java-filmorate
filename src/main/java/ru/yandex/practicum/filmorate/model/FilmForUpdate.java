package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.Property.CustomDurationDeserializer;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.validators.DateRange;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Data
public class FilmForUpdate{
    private static final Logger log = LoggerFactory.getLogger(Film.class);
    private final Set<User> likeUser = new HashSet<>();
    private long id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @DateRange(min = "1895-12-28")
    private LocalDate releaseDate;
    @DurationMin(nanos = 1)
    @JsonDeserialize(using = CustomDurationDeserializer.class)
    private Duration duration;
    @JsonAlias({"genre", "genres"})
    private List<Genre> genres = new ArrayList<>();
    @JsonProperty("mpa")
    private MPA rating;

    @JsonGetter("duration")
    public long getDurationInSeconds() {
        return duration.toSeconds();
    }

    public FilmForUpdate addLike(User user) {
        if (likeUser.add(user)) {
            log.info(String.format(
                    "Лайк пользователя %s успешно добавлен.",
                    user.getEmail()
            ));
            return this;
        }
        log.warn(String.format(
                "Пользователь %s уже ставил ранее лайк.",
                user.getEmail()
        ));
        throw new DuplicatedDataException(String.format(
                "Пользователь %s уже ставил ранее лайк.",
                user.getEmail()
        ));
    }

    public FilmForUpdate removeLike(User user) {
        likeUser.remove(user);
        log.warn(String.format(
                "Лайк пользователя %s удален.",
                user.getEmail()
        ));
        return this;
    }

    public Genre addGenre(Genre genre) {
        genres.add(genre);
        return genre;
    }

    public void dropGenre() {
        genres.clear();
    }
}