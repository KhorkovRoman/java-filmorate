package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class MpaDBStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDBStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(Integer mpaId) {
        final String sqlQuery = "SELECT * FROM mpa WHERE mpa_id = ?";
        final List<Mpa> mpa = jdbcTemplate.query(sqlQuery, MpaDBStorage::makeMpa, mpaId);
        if (mpa.size() != 1) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Mpa c id " + mpaId + " нет в базе.");
        }
        return mpa.get(0);
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        final String sqlQuery = "SELECT * FROM mpa";
        final List<Mpa> mpa = jdbcTemplate.query(sqlQuery, MpaDBStorage::makeMpa);
        return mpa;
    }

    static Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"),
                rs.getString("mpa_name"));
    }
}
