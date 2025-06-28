/*
package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public class FilmDbStorage extends BaseDbStorage<Film> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM film";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM film WHERE name = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film WHERE film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO film (name, description, release_date, duration, genre_id, rating_id)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, genre_id = ?, rating_id = ? WHERE film_id = ?";


    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    protected long save(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.ge);
        return id;
    }

    @Override
    protected List<Film> findMany(String query, Object... params) {
        return super.findMany(query, params);
    }

    @Override
    protected Optional<Film> findOne(String query, Object... params) {
        return super.findOne(query, params);
    }

    @Override
    protected void update(String query, Object... params) {
        super.update(query, params);
    }
}
*/
