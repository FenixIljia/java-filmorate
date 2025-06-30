package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM film";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM film WHERE name = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film WHERE film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO film (name, description, release_date, duration, rating_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE film_id = ?";
    private static final String DELETE_QUERY = "DELETE film WHERE film_id = ?";
    private final RatingDbStorage ratingDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final FilmGenreBdStorage filmGenreBdStorage;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, RatingDbStorage ratingDbStorage, GenreDbStorage genreDbStorage, FilmGenreBdStorage filmGenreBdStorage) {
        super(jdbc, mapper);
        this.ratingDbStorage = ratingDbStorage;
        this.genreDbStorage = genreDbStorage;
        this.filmGenreBdStorage = filmGenreBdStorage;
    }

    @Transactional
    public Film save(Film film) {
        long id;
        id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration().toMinutes(),
                film.getRating().getId()
        );
        film.setId(id);
        if (!film.getGenres().isEmpty()) {
            List<Genre> uniqueGenres = film.getGenres().stream()
                    .distinct()
                    .toList();
            film.dropGenre();
            for (Genre uniqueGenre : uniqueGenres) {
                film.addGenre(uniqueGenre);
            }
            for (Genre genre : film.getGenres()) {
                filmGenreBdStorage.create(new FilmGenre(film.getId(), genre.getId()));
            }
        }
        return film;
    }

    public FilmDto update(FilmDto film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        if (!film.getGenres().isEmpty())
            filmGenreBdStorage.delete(film.getId());
        for (Genre genre : film.getGenres()) {
            filmGenreBdStorage.create(new FilmGenre(film.getId(), genre.getId()));
        }
        return film;
    }

    public List<Film> findAll() {
        List<Film> films = findMany(FIND_ALL_QUERY);
        for (Film film : films) {
            for (FilmGenre filmGenre : filmGenreBdStorage.findOne(film.getId())) {
                film.addGenre(new Genre(filmGenre.getGenreId(), genreDbStorage.findById(filmGenre.getGenreId()).get().getName()));
            }
        }
        return films;
    }

    public Optional<Film> findById(long filmId) {
        Optional<Film> film = findOne(FIND_BY_ID_QUERY, filmId);
        for (FilmGenre filmGenre : filmGenreBdStorage.findOne(film.get().getId())) {
            film.get().addGenre(new Genre(filmGenre.getGenreId(), genreDbStorage.findById(filmGenre.getGenreId()).get().getName()));
        }
        return film;
    }

    public Optional<Film> findByName(String name) {
        return findOne(FIND_BY_NAME_QUERY, name);
    }

    public void deleteById(long film_id) {
        delete(DELETE_QUERY, film_id);
    }
}
