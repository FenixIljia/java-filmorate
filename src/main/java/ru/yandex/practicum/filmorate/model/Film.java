package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.time.DurationMin;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private long id;
    @NonNull
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDateTime releaseDate;
    @DurationMin(nanos = 0)
    private Duration duration;
}
