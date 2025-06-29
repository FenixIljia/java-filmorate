package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmForUpdate;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM film";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM film WHERE name = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film WHERE film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO film (name, description, release_date, duration, genre_id, rating_id)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, genre_id = ?, rating_id = ? WHERE film_id = ?";
    private static final String DELETE_QUERY = "DELETE film WHERE film_id = ?";
    private final RatingDbStorage ratingDbStorage;
    private final GenreDbStorage genreDbStorage;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, RatingDbStorage ratingDbStorage, GenreDbStorage genreDbStorage) {
        super(jdbc, mapper);
        this.ratingDbStorage = ratingDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    public Film save(Film film) {
        System.out.println(genreDbStorage);
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration().toMinutes(),
                film.getGenres().getFirst().getId(),
                film.getRating().getId()
        );
        film.setId(id);
        return film;
    }

    public FilmDto update(FilmDto film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
               film.getGenres().getFirst().getId(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Film> findById(long film_id) {
        return findOne(FIND_BY_ID_QUERY, film_id);
    }

    public Optional<Film> findByName(String name) {
        return findOne(FIND_BY_NAME_QUERY, name);
    }

    public void deleteById(long film_id) {
        delete(DELETE_QUERY, film_id);
    }
}
