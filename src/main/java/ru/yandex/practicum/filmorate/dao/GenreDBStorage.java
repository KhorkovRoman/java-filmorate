package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
public class GenreDBStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME"));
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        final String sqlQuery = "SELECT * FROM genres WHERE genre_id = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreDBStorage::makeGenre, genreId);
        if (genres.size() != 1) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Жанра c id " + genreId + " нет в базе.");
        }
        return genres.get(0);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        final String sqlQuery = "SELECT * FROM genres";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreDBStorage::makeGenre);
        return genres;
    }

    @Override
    public void setFilmGenres(Film film) {
        final String sqlQueryFoDel = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sqlQueryFoDel, film.getId());

        for (Genre genre: film.getGenres()) {
            String sqlQuery = "MERGE INTO film_genres(film_id, genre_id) VALUES(?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                stmt.setInt(1, film.getId());
                stmt.setInt(2, genre.getId());
                return stmt;
            }, keyHolder);
        }
    }

    @Override
    public LinkedHashSet<Genre> loadFilmGenres(Integer filmId) {
        final String sqlQuery = "SELECT * " +
                " FROM genres AS ge" +
                " LEFT OUTER JOIN film_genres AS fg ON ge.genre_id = fg.genre_id" +
                " WHERE film_id = ?";
        final LinkedHashSet<Genre> genres =
                new LinkedHashSet<>(jdbcTemplate.query(sqlQuery, GenreDBStorage::makeGenre, filmId));

        return genres;
    }

}
