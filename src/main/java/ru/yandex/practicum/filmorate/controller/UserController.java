package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
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
    public Collection<UserDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserDto find(@PathVariable long id) {
        return UserMapper.mapToUserDto(service.find(id));
    }

    @PostMapping
    public UserDto create(@RequestBody @Valid User user) {
        return UserMapper.mapToUserDto(service.create(user));
    }

    @PutMapping
    public UserDto update(@RequestBody @Valid User user) {
        return UserMapper.mapToUserDto(service.update(user));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable long id, @PathVariable long friendId) {
        service.removeFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> findAllFriends(@PathVariable long id) {
        return service.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getGeneralFriends(@PathVariable long id, @PathVariable long otherId) {
        return service.getCommonFriends(id, otherId);
    }
}
