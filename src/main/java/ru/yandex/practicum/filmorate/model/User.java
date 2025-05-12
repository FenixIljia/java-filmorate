package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class User {
    private long id;
    @Email
    @NotNull
    private String email;
    @NotBlank
    @Pattern(regexp = "^\\S+$")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
