package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.ValidationFilm;

import java.util.*;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final ValidationFilm validationFilm;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       ValidationFilm validationFilm) {
        this.filmStorage = filmStorage;
        this.validationFilm = validationFilm;
    }

    public Film createFilm(Film film) {
        validationFilm.validateFilm(film);
        return filmStorage.createFilm(film);
    }

    public Film getFilm(Integer filmId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Фильма c id " + filmId + " нет в базе.");
        }
        log.info("Фильм c id " + filmId + " найден в базе.");
        return film;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film updateFilm(Film film) {
        validateFilm(film.getId());
        validationFilm.validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(Film film) {
        filmStorage.deleteFilm(film);
    }

    public void validateFilm(Integer filmId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Фильма c id " + filmId + " нет в базе.");
        }
    }
}
