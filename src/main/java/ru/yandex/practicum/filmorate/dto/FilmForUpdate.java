package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.time.DurationMin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.DateRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class FilmForUpdate {
    private static final Logger log = LoggerFactory.getLogger(Film.class);
    private final Set<User> likeUser = new HashSet<>();
    private long id;
    private String name;
    @Size(max = 200)
    private String description;
    @DateRange(min = "1895-12-28")
    private LocalDate releaseDate;
    @DurationMin(nanos = 1)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private long duration;
    @JsonAlias({"genre", "genres", "rate"})
    private List<Genre> genres = new ArrayList<>();
    @JsonAlias("mpa")
    private MPA rating;

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

/*    @JsonProperty("duration")
    public long getDurationMinutes() {
        return duration != null ? duration.toMinutes() : 0;
    }

    @JsonProperty("duration")
    public void setDurationMinutes(long minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }*/
}