package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.LikeUserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.LikeUser;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class LikeUserBdStorage extends BaseDbStorage<LikeUser> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM like_user";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT * FROM like_user WHERE film_id = ?";
    private static final String FIND_BY_USER_ID_QUERY = "SELECT * FROM like_user WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO like_user (film_id, user_id) VALUES (?, ?)";
    private static final String UPDATE_BY_USER_ID_QUERY = "UPDATE like_user SET film_id = ?, user_id = ? WHERE user_id = ?";
    private static final String UPDATE_BY_FILM_ID_QUERY = "UPDATE like_user SET film_id = ?, user_id = ? WHERE film_id = ?";
    private static final String DELETE_BY_FILM_ID_QUERY = "DELETE like_user WHERE film_id = ?";
    private static final String DELETE_BY_USER_ID_QUERY = "DELETE like_user WHERE user_id = ?";

    public LikeUserBdStorage(JdbcTemplate jdbc, LikeUserRowMapper mapper) {
        super(jdbc, mapper);
    }

    public LikeUser crate(long film, long user) {
        insert(INSERT_QUERY, film, user);
        LikeUser likeUser = new LikeUser();
        likeUser.setUserId(user);
        likeUser.setFilmId(film);
        return likeUser;
    }

    public List<LikeUser> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<LikeUser> findByFilmId(Film film) {
        return findOne(FIND_BY_FILM_ID_QUERY, film.getId());
    }

    public Optional<LikeUser> findByUserId(User user) {
        return findOne(FIND_BY_USER_ID_QUERY, user.getId());
    }

    public LikeUser updateByUserId(User user, Film film) {
        update(UPDATE_BY_USER_ID_QUERY, film.getId(), user.getId(), user.getId());
        LikeUser likeUser = new LikeUser();
        likeUser.setFilmId(film.getId());
        likeUser.setUserId(user.getId());
        return likeUser;
    }

    public LikeUser updateByFilmId(long user, long film) {
        update(UPDATE_BY_FILM_ID_QUERY, film, user, film);
        LikeUser likeUser = new LikeUser();
        likeUser.setFilmId(film);
        likeUser.setUserId(user);
        return likeUser;
    }

    public void deleteByFilmId(long film) {
        delete(DELETE_BY_FILM_ID_QUERY, film);
    }

    public void deleteByUserId(long user) {
        delete(DELETE_BY_USER_ID_QUERY, user);
    }
}
