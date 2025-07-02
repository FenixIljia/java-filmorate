package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

@Repository
public class RatingDbStorage extends BaseDbStorage<RatingDto> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM rating";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM rating WHERE name = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating WHERE rating_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO rating (name) VALUES (?)";
    private static final String UPDATE_QUERY = "UPDATE rating SET name = ? WHERE rating_id = ?";
    private static final String DELETE_QUERY = "DELETE rating WHERE rating_id = ?";

    public RatingDbStorage(JdbcTemplate jdbc, RowMapper<RatingDto> mapper) {
        super(jdbc, mapper);
    }

    public long save(Rating rating) {
        return insert(INSERT_QUERY, rating.toString());
    }

    public Optional<RatingDto> findById(long ratingId) {
        return findOne(FIND_BY_ID_QUERY, ratingId);
    }

    public Optional<RatingDto> findByName(String name) {
        return findOne(FIND_BY_NAME_QUERY, name);
    }

    public List<RatingDto> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public void update(long ratingId) {
        update(UPDATE_QUERY, ratingId);
    }

    public void deleteById(long ratingId) {
        delete(DELETE_QUERY, ratingId);
    }
}
