package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class FriendsDbStorage {
    private final JdbcTemplate jdbc;

    public FriendsDbStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    public void addFriendship(long userId, long friendId) {
        jdbc.update("INSERT INTO friends (user_id, friend_id) VALUES (?, ?)", userId, friendId);
//        jdbc.update("INSERT INTO friends (user_id, friend_id) VALUES (?, ?)", friendId, userId);
    }

    @Transactional
    public void removeFriendship(long userId, long friendId) {
        jdbc.update("DELETE FROM friends WHERE (user_id = ? AND friend_id = ?)",
                userId, friendId
        );
    }

    public List<Long> getFriendsIds(long userId) {
        return jdbc.queryForList(
                "SELECT friend_id FROM friends WHERE user_id = ?",
                Long.class,
                userId
        );
    }

    public List<Long> getCommonFriendsIds(long userId1, long userId2) {
        return jdbc.queryForList("""
            SELECT f1.friend_id
            FROM friends f1
            JOIN friends f2 ON f1.friend_id = f2.friend_id
            WHERE f1.user_id = ? AND f2.user_id = ?
            """,
                Long.class,
                userId1, userId2
        );
    }

    public boolean friendshipExists(long userId, long friendId) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM friends WHERE user_id = ? AND friend_id = ?",
                Integer.class,
                userId, friendId
        );
        return count != null && count > 0;
    }
}