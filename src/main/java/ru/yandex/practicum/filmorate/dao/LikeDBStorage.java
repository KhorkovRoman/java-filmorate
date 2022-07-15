package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
@Component
public class LikeDBStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public LikeDBStorage(JdbcTemplate jdbcTemplate, FilmStorage filmStorage, UserStorage userStorage){
        this.jdbcTemplate=jdbcTemplate;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        String sqlQuery = "MERGE INTO FILM_LIKES(FILM_ID, USER_ID) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setInt(1, film.getId());
            stmt.setInt(2, user.getId());
            return stmt;
        }, keyHolder);

        log.info("Фильм c id " + filmId + " успешно лайкнут " +
                "пользователем " + userId + ".");
        return film;
    }

    public void deleteLike(Integer filmId, Integer userId) {
        final String sqlQuery = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);

        log.info("У фильма c id " + filmId + " успешно удален лайк " +
                "пользователя с id " + userId + ".");
    }

    public List<Film> getPopularFilms(Integer count) {
        String sqlQuery = "SELECT *" +
                " FROM FILMS AS F" +
                " LEFT OUTER JOIN FILM_LIKES AS Fl ON F.FILM_ID = Fl.FILM_ID" +
                " GROUP BY F.FILM_ID" +
                " ORDER BY COUNT(DISTINCT FL.USER_ID) DESC" +
                " LIMIT ?";
        return jdbcTemplate.query(sqlQuery, FilmDBStorage::makeFilm, count);
    }
}
