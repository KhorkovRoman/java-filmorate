package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class UserDBStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDBStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "INSERT INTO users(email, login, user_name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            if (user.getName().isBlank()) {
                if (!user.getLogin().isBlank()) {
                    user.setName(user.getLogin());
                }
            }
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());

        return user;
    }

    @Override
    public User getUserById(Integer userId) {
        final String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDBStorage::makeUser, userId);
        if (users.size() != 1) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "???????????????????????? c id " + userId + " ?????? ?? ????????.");
        }
        return users.get(0);
    }

    @Override
    public Collection<User> getAllUsers() {
        final String sqlQuery = "SELECT * FROM users";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDBStorage::makeUser);
        return users;
    }

    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("user_name"),
                rs.getDate("birthday").toLocalDate());
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET " +
                "email = ?, login = ?, user_name = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        return user;
    }

    @Override
    public void deleteUser(User user) {
        final String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getId());
    }
}
