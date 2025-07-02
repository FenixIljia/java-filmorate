package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

@Repository
public class FilmGenreBdStorage extends BaseDbStorage<FilmGenre> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM film_genre";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film_genre WHERE film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO film_genre (film_id, genre_id)" +
            "VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE film_genre SET genre_id = ? WHERE film_id = ?";
    private static final String DELETE_QUERY = "DELETE film_genre WHERE film_id = ?";

    public FilmGenreBdStorage(JdbcTemplate jdbcTemplate, RowMapper<FilmGenre> mapper) {
        super(jdbcTemplate, mapper);
    }

    public List<FilmGenre> findOne(long filmId) {
        return findMany(FIND_BY_ID_QUERY, filmId);
    }

    public List<FilmGenre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Transactional
    public void create(FilmGenre filmGenre) {
        jdbc.update(INSERT_QUERY, filmGenre.getFilmId(), filmGenre.getGenreId());
    }

    public void update(FilmGenre filmGenre) {
        update(UPDATE_QUERY, filmGenre.getGenreId(), filmGenre.getFilmId());
    }

    public void delete(long id) {
        delete(DELETE_QUERY, id);
    }
}
