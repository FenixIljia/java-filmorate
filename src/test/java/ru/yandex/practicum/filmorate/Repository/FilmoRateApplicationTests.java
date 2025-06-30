package ru.yandex.practicum.filmorate.Repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.util.Optional;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class})
class FilmoRateApplicationTests {
    @Autowired
    private final UserDbStorage userStorage;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        jdbcTemplate.update(
                "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)", "test@mail.ru", "testLogin", "Test Name", Date.valueOf("1990-01-01")
        );
        Optional<User> userOptional = userStorage.findById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }
}