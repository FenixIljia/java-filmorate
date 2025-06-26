package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public Collection<User> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public User find(@PathVariable long id) {
        return service.find(id);
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return service.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        return service.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Set<User> addFriend(@PathVariable long id, @PathVariable long friendId) {
        return service.addFriend(id, friendId);
    }

/*    @DeleteMapping("/{id}/friends/{friendId}")
    public Set<User> removeFriend(@PathVariable long id, @PathVariable long friendId) {
        return service.removeFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> findAllFriends(@PathVariable long id) {
        return service.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getGeneralFriends(@PathVariable long id, @PathVariable long otherId) {
        return service.getGeneralFriends(id, otherId);
    }*/
}
