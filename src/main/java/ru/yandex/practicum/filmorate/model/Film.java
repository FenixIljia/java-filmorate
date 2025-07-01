package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.validators.DateRange;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    private static final Logger log = LoggerFactory.getLogger(Film.class);
    private final Set<Long> likeUser = new HashSet<>();
    private long id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @DateRange(min = "1895-12-28")
    private LocalDate releaseDate;
    @DurationMin(nanos = 1)
    private Duration duration;
    @JsonAlias({"genre", "genres"})
    private List<Genre> genres = new ArrayList<>();
    @JsonProperty("mpa")
    private MPA rating;


    public Film addLike(long user) {
        if (likeUser.add(user)) {
            log.info(String.format(
                    "Лайк пользователя %d успешно добавлен.",
                    user
            ));
            return this;
        }
        log.warn(String.format(
                "Пользователь %d уже ставил ранее лайк.",
                user
        ));
        throw new DuplicatedDataException(String.format(
                "Пользователь %d уже ставил ранее лайк.",
                user
        ));
    }

    public Film removeLike(User user) {
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

    @JsonGetter("duration")
    public long getDurationInMinutes() {
        return duration.toMinutes();
    }

    @JsonSetter("duration")
    public void setDurationFromMinutes(long minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }
}