package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.LikeUser;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeUserRowMapper implements RowMapper<LikeUser> {
    @Override
    public LikeUser mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        LikeUser likeUser = new LikeUser();
        likeUser.setFilmId(resultSet.getLong("film_id"));
        likeUser.setUserId(resultSet.getLong("user_id"));
        return likeUser;
    }
}
