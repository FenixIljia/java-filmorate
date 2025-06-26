package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users;

    public Collection<User> findAll() {
        log.info("Получение всех пользователь из базы данных.");
        return users.values();
    }

    public User find(long id) {
        if (users.containsKey(id)) {
            log.info(String.format("Пользователь с id %d успешно найден.", id));
            return users.get(id);
        }
        log.warn(String.format("Пользователь с id %d не найден.", id));
        throw new NotFoundException(String.format("Пользователь с id %d не найден.", id));
    }

    public User create(User user) {
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

    public User update(User user) {
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
