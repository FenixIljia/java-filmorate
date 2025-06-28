package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.FriendsDbStorage;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserDbStorage storage;
    private final FriendsDbStorage friendsDbStorage;

    public Collection<UserDto> findAll() {
        return storage.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    // Метод добавляет пользователь в друзья друг другу
    @Transactional
    public void addFriend(long userId, long friendId) {
        // Проверка существования пользователей
        storage.getById(userId);
        storage.getById(friendId);

        // Проверка, что дружба ещё не существует
        if (friendsDbStorage.friendshipExists(userId, friendId)) {
            throw new DuplicatedDataException("Дружба уже существует");
        }

        // Создание взаимной дружбы
        friendsDbStorage.addFriendship(userId, friendId);
    }
    // Метод удаляет пользователь из друзе друг друга
    @Transactional
    public void removeFriends(long userId, long friendId) {
        // Проверка существования пользователей
        User user = storage.getById(userId);
        User friend = storage.getById(friendId);

        // Удаление взаимной дружбы
        friendsDbStorage.removeFriendship(userId, friendId);

        log.info("Пользователи {} и {} удалены из друзей друг друга",
                user.getEmail(), friend.getEmail());
    }

    // Метод возвращает всех друзей конктретного пользователя
    public Set<User> findAllFriends(long userId) {
        User user = storage.getById(userId);
        List<Long> friendIds = friendsDbStorage.getFriendsIds(userId);

        return friendIds.stream()
                .map(storage::getById)
                .collect(Collectors.toSet());
    }

    // Метод возвращает общих друзей двух пользователей
    public Set<User> getCommonFriends(long userId1, long userId2) {
        // Проверка существования пользователей
        storage.getById(userId1);
        storage.getById(userId2);

        List<Long> commonFriendIds = friendsDbStorage.getCommonFriendsIds(userId1, userId2);

        return commonFriendIds.stream()
                .map(storage::getById)
                .collect(Collectors.toSet());
    }

    public User find(long id) {
        return storage.findById(id).get();
    }

    public User create(User user) {
        return storage.save(user);
    }

    public User update(User user) {
        return storage.update(user);
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
