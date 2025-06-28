package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public class GenreDbStorage extends BaseDbStorage<Genre> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM genre WHERE name = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO genre (name) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE genre SET name = ? WHERE genre_id = ?";
    private static final String DELETE_QUERY = "DELETE genre WHERE genre_id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public long save(Genre genre) {
        return insert(
                INSERT_QUERY,
                genre.toString()
                );
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Genre> findById(long genre_id) {
        return findOne(FIND_BY_ID_QUERY, genre_id);
    }

    public Optional<Genre> findByName(String name) {
        return findOne(FIND_BY_NAME_QUERY, name);
    }

    public void update(long genre_id) {
        update(UPDATE_QUERY, genre_id);
    }

    public void deleteById(long genre_id) {
        delete(DELETE_QUERY, genre_id);
    }
}
