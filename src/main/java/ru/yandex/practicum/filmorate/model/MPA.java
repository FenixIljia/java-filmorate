package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MPA {
    long id;

    @JsonCreator
    public MPA(@JsonProperty("id") int id) {
        this.id = id;
    }
}
