package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class FriendDBStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Autowired
    public FriendDBStorage(JdbcTemplate jdbcTemplate, UserStorage userStorage){
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage  = userStorage;
    }

    public FriendStatus getStatusFriendship(Integer friendStatusId) {
        final String sqlQuery = "SELECT * FROM friend_statuses WHERE friend_status_id = ?";
        final List<FriendStatus> statuses = jdbcTemplate.query(sqlQuery, FriendDBStorage::makeStatus, friendStatusId);
        return statuses.get(0);
    }

    static FriendStatus makeStatus(ResultSet rs, int rowNum) throws SQLException {
        return new FriendStatus(rs.getInt("friend_status_id"),
                rs.getString("friend_status_name"));
    }

    public void updateFriendship(User friend, User user, Integer status_id) {
        String sqlQuery = "MERGE INTO friendship(user_id, friend_id, friend_status_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setInt(1, friend.getId());
            stmt.setInt(2, user.getId());
            stmt.setInt(3, status_id);
            return stmt;
        }, keyHolder);
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        User user   = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        FriendStatus friendStatusUnConfirmed = getStatusFriendship(1);
        FriendStatus friendStatusConfirmed = getStatusFriendship(2);

        String sqlQuery = "MERGE INTO friendship(user_id, friend_id, friend_status_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setInt(1, user.getId());
            stmt.setInt(2, friend.getId());

            if (getUserFriends(friendId).contains(user)) {
                stmt.setInt(3, friendStatusConfirmed.getId());
                updateFriendship(friend, user, friendStatusConfirmed.getId());
            } else {
                stmt.setInt(3, friendStatusUnConfirmed.getId());
            }
            return stmt;
        }, keyHolder);

        log.info("Пользователь c id " + friendId + " успешно записан " +
                "в друзья к " + user + ".");

        return user;
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        final String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);

        log.info("Пользователь c id " + friendId + " успешно удален " +
                "из друзей пользователя с id " + userId + ".");
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        final String sqlQuery = "SELECT * " +
                " FROM users AS u" +
                " JOIN friendship AS f ON u.user_id = f.friend_id" +
                " WHERE f.user_id = ?";
        return jdbcTemplate.query(sqlQuery, UserDBStorage::makeUser, userId);
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        final String sqlQuery = "SELECT * " +
                " FROM users AS U " +
                " JOIN friendship AS fu ON u.user_id = fu.friend_id " +
                " JOIN friendship AS fo ON u.user_id = fo.friend_id " +
                " WHERE fu.user_id = ? AND fo.user_id = ?";
        return jdbcTemplate.query(sqlQuery, UserDBStorage::makeUser, userId, otherId);
    }
}
