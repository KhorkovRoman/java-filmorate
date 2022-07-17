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
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class FilmDBStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public FilmDBStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage,
                         GenreStorage genreStorage){
        this.jdbcTemplate=jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Collection<Film> getAllFilms() {
        final String sqlQuery = "SELECT * FROM films";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDBStorage::makeFilm);
        return films;
    }

    @Override
    public Film getFilmById(Integer filmId) {
        final String sqlQuery = "SELECT * FROM films WHERE film_id = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDBStorage::makeFilm, filmId);
        if (films.size() != 1) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Фильма c id " + filmId + " нет в базе.");
        }
        Film film = films.get(0);
        Mpa mpa = mpaStorage.getMpaById(film.getMpa().getId());
        film.setMpa(mpa);

        Set<Genre> genres = genreStorage.loadFilmGenres(filmId);
        Set<Genre> genreList = film.getGenres();
        genreList.addAll(genres);
        return film;
    }

    static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("film_id"),
                rs.getString("film_name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                rs.getInt("rate"),
                new Mpa(rs.getInt("mpa_id")));
    }

    @Override
    public Film createFilm(Film film) {
        Integer mpaId = film.getMpa().getId();

        String sqlQuery = "INSERT INTO films(film_name, description, " +
                "release_date, duration, rate, mpa_id) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setInt(6, mpaId);
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());

        Mpa mpa = mpaStorage.getMpaById(mpaId);
        film.setMpa(mpa);

        genreStorage.setFilmGenres(film);
        LinkedHashSet<Genre> genres = genreStorage.loadFilmGenres(film.getId());
        film.setGenres(genres);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Integer mpaId = film.getMpa().getId();

        String sqlQuery = "UPDATE films " +
                "SET film_name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getRate()
                , mpaId
                , film.getId());
        Mpa mpa = mpaStorage.getMpaById(mpaId);
        film.setMpa(mpa);
        genreStorage.setFilmGenres(film);
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        final String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }
}
