package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
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
                throw new ValidationException("Пользователь с email " + user.getEmail() + " уже существует");
            }
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }

        User oldUser = users.get(user.getId());
        if (user.getLogin() != null) {
            if (oldUser.getName().equals(oldUser.getLogin())) {
                oldUser.setName(user.getLogin());
            }
            oldUser.setLogin(user.getLogin());
        }
        if (user.getBirthday() != null) {
            oldUser.setBirthday(user.getBirthday());
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        oldUser.setEmail(user.getEmail());
        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
