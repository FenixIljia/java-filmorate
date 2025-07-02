package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

//Пользователь
@Data
@Slf4j
@ToString(exclude = "friends")
@EqualsAndHashCode(of = {"email"})
public class User {
    @JsonIgnore
    private Set<Long> friends = new HashSet<>();
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

    public User addFriends(User user) {
        if (friends.add(user.getId())) {
            log.info(String.format(
                    "Пользователь %s успешно добавлен в друзья пользователю %s",
                    user.getEmail(),
                    this.getEmail()
            ));
            return user;
        }

        log.warn(String.format(
                "Пользователь %s уже добавлен в друзья пользователю %s",
                user.getEmail(),
                this.getEmail()
        ));
        throw new DuplicatedDataException(String.format(
                "Пользователь %s уже добавлен в друзья пользователю %s",
                user.getEmail(),
                this.getEmail()
        ));
    }

    public User removeFriends(User user) {
        friends.remove(user.getId());
        log.info(String.format(
                "Пользователь %s удален из друзей пользователя %s",
                user.getEmail(),
                this.getEmail()
        ));
        return user;
    }
}
