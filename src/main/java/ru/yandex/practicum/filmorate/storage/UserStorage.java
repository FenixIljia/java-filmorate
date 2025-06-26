package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User find(long id);

    Collection<User> findAll();

    User create(User user);

    User update(User user);
}
