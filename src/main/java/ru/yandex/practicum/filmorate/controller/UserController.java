package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        for (User value : users.values()) {
            if (value.getEmail().equals(user.getEmail())) {
                log.warn("Попытка добавить нового пользователя с email, который уже есть в базе - {}.", user.getEmail());
                throw new ValidationException("Пользователь с email " + user.getEmail() + " уже существует");
            }
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.debug(
                    "У нового пользователя {} не заполнено поле name. Полю name присвоено заначение поля login - {}.",
                    user.getLogin(),
                    user.getLogin()
            );
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь - {}", user.getLogin());
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с id ${} не найден в базе", user.getId());
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }

        User oldUser = users.get(user.getId());
        if (user.getLogin() != null) {
            if (oldUser.getName().equals(oldUser.getLogin())) {
                oldUser.setName(user.getLogin());
                log.debug("У пользователя {} обновлено имя - {}.", oldUser.getLogin(), oldUser.getName());
            }
            log.debug("У пользователя {} обновлен логин - {}.", oldUser.getLogin(), user.getLogin());
            oldUser.setLogin(user.getLogin());
        }
        if (user.getBirthday() != null) {
            oldUser.setBirthday(user.getBirthday());
            log.debug("У пользователя {} обновлена дата рождения - {}.", oldUser.getLogin(), oldUser.getBirthday());
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
            log.debug("У пользователя {} обновлена дата рождения - {}.", oldUser.getLogin(), oldUser.getBirthday());
        }
        oldUser.setEmail(user.getEmail());
        log.info("Данные пользователя {} успешно обновлены!", oldUser.getLogin());
        return oldUser;
    }

    //Генерация id
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}