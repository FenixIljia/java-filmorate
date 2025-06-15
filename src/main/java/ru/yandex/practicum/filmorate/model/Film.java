package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.validators.DateRange;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
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
    private Duration duration;

    public Film addLike(User user) {
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

    public Film removeLike(User user) {
        likeUser.remove(user);
        log.warn(String.format(
                "Лайк пользователя %s удален.",
                user.getEmail()
        ));
        return this;
    }

    @JsonGetter("duration")
    public long getDurationInSeconds() {
        return duration.toSeconds();
    }
}
