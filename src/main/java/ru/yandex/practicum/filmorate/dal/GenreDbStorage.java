package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseDbStorage<GenreDto> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM genre WHERE name = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO genre (name) VALUES (?)";
    private static final String UPDATE_QUERY = "UPDATE genre SET name = ? WHERE genre_id = ?";
    private static final String DELETE_QUERY = "DELETE genre WHERE genre_id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<GenreDto> mapper) {
        super(jdbc, mapper);
    }

    public GenreDto save(Genre genre) {
        long id = insert(
                INSERT_QUERY,
                genre.toString()
                );
        GenreDto genreDto = new GenreDto();
        genreDto.setGenre_id(id);
        genreDto.setName(genre.toString());
        return genreDto;
    }

    public List<GenreDto> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<GenreDto> findById(long genre_id) {
        return findOne(FIND_BY_ID_QUERY, genre_id);
    }

    public Optional<GenreDto> findByName(String name) {
        return findOne(FIND_BY_NAME_QUERY, name);
    }

    public void update(long genre_id) {
        update(UPDATE_QUERY, genre_id);
    }

    public void deleteById(long genre_id) {
        delete(DELETE_QUERY, genre_id);
    }
}
