package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage storage;

    // Метод добавляет пользователь в друзья друг другу
    public Set<User> addFriend(long user, long friends) {
        validation(user, friends);
        User user1 = storage.find(user);
        User friend = storage.find(friends);
        user1.addFriends(friend);
        friend.addFriends(user1);
        log.info(String.format(
                "Пользователи %s и %s успешно добавлены друг другу в друзья",
                user1.getEmail(),
                friend.getEmail()
        ));
        return Set.of(user1, friend);
    }

    // Метод удаляет пользователь из друзе друг друга
    public Set<User> removeFriends(long user, long friends) {
        validation(user, friends);
        User user1 = storage.find(user);
        User friend = storage.find(friends);
        user1.removeFriends(friend);
        friend.removeFriends(user1);
        log.info(String.format(
                "Пользователи %s и %s удалены из друзей друг друга.",
                storage.find(user).getEmail(),
                storage.find(friends).getEmail()
        ));
        return Set.of(storage.find(user), storage.find(friends));
    }

    // Метод возвращает общих друзей двух пользователей
    public Set<User> getGeneralFriends(long user1, long user2) {
        validation(user1, user2);
        final Set<User> generalFriends = new HashSet<>();
        for (Long friend : storage.find(user1).getFriends()) {
            for (Long user : storage.find(user2).getFriends()) {
                if (storage.find(friend).equals(storage.find(user))) {
                    log.info(String.format(
                            "У пользователей %s и %s найден общий друг %s.",
                            storage.find(user1).getEmail(),
                            storage.find(user2).getEmail(),
                            storage.find(user)
                    ));
                    generalFriends.add(storage.find(user));
                }
            }
        }
        log.info(
                "Получен список общих друзей у пользовтелей {} и {}",
                storage.find(user1).getEmail(),
                storage.find(user2).getEmail()
        );
        return generalFriends;
    }

    // Метод возвращает всех друзей конктретного пользователя
    public Set<User> findAllFriends(long user) {
        if (user < 0) {
            log.warn("Параметр user не может быть ниже нуля. Текущее значение - {}", user);
            throw new ValidationException("Параметр user не может быть ниже нуля - " + user);
        }
        final Set<User> users = new HashSet<>();
        for (Long friend : storage.find(user).getFriends()) {
            users.add(storage.find(friend));
        }
        log.info("Получен список всех друзей пользователя {}", storage.find(user).getEmail());
        return users;
    }

    private void validation(long user, long friends) {
        if (user < 0) {
            log.warn("user не может быть ниже нуля. Текущее значение - {}", user);
            throw new ValidationException("user не может быть ниже нуля. Текущее значение - " + user);
        }
        if (friends < 0) {
            log.warn("friends не может быть ниже нуля. Текущее значение - {}", friends);
            throw new ValidationException("friends не может быть ниже нуля. Текущее значение - " + friends);
        }
    }
}
