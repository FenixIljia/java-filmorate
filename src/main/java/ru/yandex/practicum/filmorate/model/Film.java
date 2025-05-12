package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.time.DurationMin;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private long id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @DurationMin(nanos = 1)
    private Duration duration;

    @JsonGetter("duration")
    public long getDurationInSeconds() {
        return duration.toSeconds();
    }
}
