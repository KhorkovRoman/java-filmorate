package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    ValidationFilm validationFilm;
    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Autowired
    public InMemoryFilmStorage(ValidationFilm validationFilm) {
        this.validationFilm = validationFilm;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public Film getFilmById(Integer filmId) {
        return films.get(filmId);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        validationFilm.validateFilm(film);
        filmId++;
        film.setId(filmId);
        films.put(film.getId(), film);
        log.info("Фильм " + film + " успешно записан.");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            validationFilm.validateFilm(film);
            films.put(film.getId(), film);
            log.info("Фильм " + film + " успешно обновлен.");
        } else {
            log.warn("Фильма с id " + film.getId() + " в базе нет.");
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Фильма с id " + film.getId() + " в базе нет.");
        }
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        if (films.containsKey(film.getId())) {
            validationFilm.validateFilm(film);
            films.remove(film.getId());
            log.info("Фильм " + film + " успешно удален.");
        } else {
            log.warn("Фильма с id " + film.getId() + " в базе нет.");
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Фильма с id " + film.getId() + " в базе нет.");
        }
    }

}
