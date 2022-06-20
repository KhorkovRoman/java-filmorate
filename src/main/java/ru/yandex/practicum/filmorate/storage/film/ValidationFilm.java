package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
@Component
public class ValidationFilm {
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
