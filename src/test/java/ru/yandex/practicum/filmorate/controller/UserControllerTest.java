package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private Validator validator;
    private User baseUser;

    @BeforeEach
    void setUp() {
        userController = new UserController();

        // Инициализация валидатора
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        baseUser = User.builder()
                .email("valid@mail.ru")
                .login("validLogin")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
    }

    // Тесты валидации
    @Test
    void shouldFailValidationForInvalidEmail() {
        User user = baseUser.toBuilder().email("invalid-email").build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationForBlankLogin() {
        User user = baseUser.toBuilder().login("  ").build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationForLoginWithSpaces() {
        User user = baseUser.toBuilder().login("login with spaces").build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationForFutureBirthday() {
        User user = baseUser.toBuilder()
                .birthday(LocalDate.now().plusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    // Тесты бизнес-логики
    @Test
    void shouldGenerateSequentialIds() {
        User user1 = userController.create(baseUser);
        User user2 = userController.create(baseUser.toBuilder().email("another@mail.ru").build());

        assertEquals(1L, user1.getId());
        assertEquals(2L, user2.getId());
    }

    @Test
    void shouldAutoSetNameAsLoginWhenNameIsNull() {
        User user = baseUser.toBuilder().name(null).build();
        User created = userController.create(user);
        assertEquals(user.getLogin(), created.getName());
    }

    @Test
    void shouldThrowOnDuplicateEmail() {
        userController.create(baseUser);
        User duplicate = baseUser.toBuilder().login("differentLogin").build();

        assertThrows(ValidationException.class, () -> userController.create(duplicate));
    }

    @Test
    void shouldUpdateUserCorrectly() {
        User created = userController.create(baseUser);
        User update = created.toBuilder()
                .login("newLogin")
                .email("new@mail.ru")
                .name("New Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        User updated = userController.update(update);
        assertEquals("newLogin", updated.getLogin());
        assertEquals("new@mail.ru", updated.getEmail());
        assertEquals("New Name", updated.getName());
        assertEquals(LocalDate.of(2000, 1, 1), updated.getBirthday());
    }

    @Test
    void shouldThrowOnUpdateNonExistingUser() {
        User nonExisting = baseUser.toBuilder().id(999L).build();
        assertThrows(NotFoundException.class, () -> userController.update(nonExisting));
    }

    @Test
    void shouldPartialUpdateUser() {
        User created = userController.create(baseUser);
        User update = User.builder()
                .id(created.getId())
                .login("partialUpdate")
                .build();

        User updated = userController.update(update);
        assertEquals("partialUpdate", updated.getLogin());
        assertEquals(created.getEmail(), updated.getEmail());
    }

    @Test
    void shouldReturnAllUsers() {
        userController.create(baseUser);
        userController.create(baseUser.toBuilder().email("another@mail.ru").build());

        assertEquals(2, userController.findAll().size());
    }

    @Test
    void shouldHandleEmptyNameOnUpdate() {
        User created = userController.create(baseUser);
        User update = created.toBuilder().name(null).build();

        User updated = userController.update(update);
        assertEquals(created.getLogin(), updated.getName());
    }
}