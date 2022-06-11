package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private int filmId = 0;

    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validateFilm(film);
        filmId++;
        film.setId(filmId);
        films.put(film.getId(), film);
        log.info("Фильм " + film + " успешно записан.");
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            validateFilm(film);
            films.put(film.getId(), film);
            log.info("Фильм " + film + " успешно обновлен.");
        } else {
            log.warn("Фильма с id " + film.getId() + " в базе нет.");
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Фильма с id " + film.getId() + " в базе нет.");
        }

        return film;
    }

    public void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Не введено название фильма.");
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания 200 символов. Введено символов: {}.",
                    film.getDescription().length());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Максимальная длина описания 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза должна быть не раньше 28 декабря 1895 года. Введено {}",
                    film.getReleaseDate());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть положительной. Введено {}",
                    film.getDuration());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Продолжительность фильма должна быть положительной.");
        }
    }
}