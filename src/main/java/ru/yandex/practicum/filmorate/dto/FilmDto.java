package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Slf4j
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private final Set<Long> likeUser = new HashSet<>();
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    private List<Genre> genres = new ArrayList<>();
    private MPA mpa;

    public void addGenres(Genre genreDto) {
        genres.add(genreDto);
    }

    public void dropGenre() {
        genres.clear();
    }

    public void addLike(long user) {
        likeUser.add(user);
    }

    @JsonGetter("duration")
    public long getDurationInMinutes() {
        return duration;
    }

    @JsonSetter("duration")
    public void setDurationFromMinutes(long minutes) {
        this.duration = minutes;
    }
}
