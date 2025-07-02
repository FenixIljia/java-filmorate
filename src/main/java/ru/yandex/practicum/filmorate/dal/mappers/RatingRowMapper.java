package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.RatingDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingRowMapper implements RowMapper<RatingDto> {
    @Override
    public RatingDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        RatingDto ratingDto = new RatingDto();
        ratingDto.setId(resultSet.getLong("rating_id"));
        ratingDto.setName(resultSet.getString("name"));
        return ratingDto;
    }
}
