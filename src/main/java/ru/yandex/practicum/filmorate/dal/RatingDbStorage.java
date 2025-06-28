package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

public class RatingDbStorage extends BaseDbStorage<Rating> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM rating";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM rating WHERE name = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating WHERE rating_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO rating (name) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE rating SET name = ? WHERE rating_id = ?";
    private static final String DELETE_QUERY = "DELETE rating WHERE rating_id = ?";

    public RatingDbStorage(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
        super(jdbc, mapper);
    }

    public long save(Rating rating) {
        return insert(INSERT_QUERY, rating.toString());
    }

    public Optional<Rating> findById(long rating_id) {
        return findOne(FIND_BY_ID_QUERY, rating_id);
    }

    public Optional<Rating> findByName(String name) {
        return findOne(FIND_BY_NAME_QUERY, name);
    }

    public List<Rating> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public void update(long rating_id) {
        update(UPDATE_QUERY, rating_id);
    }

    public void deleteById(long rating_id) {
        delete(DELETE_QUERY, rating_id);
    }
}
