package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(Film film) {
        filmStorage.deleteFilm(film);
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

    public Film addLike(Integer filmId, Integer userId) {
        if (userStorage.getUsers().containsKey(userId)) {
            Film film = filmStorage.getFilmById(filmId);
            if (film == null) {
                throw new ValidationException(HttpStatus.NOT_FOUND,
                        "Фильма c id " + filmId + " нет в базе.");
            }
            Set<Integer> filmLikes = film.getLikes();
            filmLikes.add(userId);
            log.info("Фильм c id " + filmId + " успешно лайкнут " +
                    "пользователем " + userId + ".");
            return film;
        } else {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Пользователя c id " + userId + " нет в базе.");
        }
    }

    public void deleteLike(Integer filmId, Integer userId) {
        if (userStorage.getUsers().containsKey(userId)) {
            Film film = filmStorage.getFilmById(filmId);
            if (film == null) {
                throw new ValidationException(HttpStatus.NOT_FOUND,
                        "Фильма c id " + filmId + " нет в базе.");
            }
            Set<Integer> filmLikes = film.getLikes();
            filmLikes.remove(userId);

            log.info("У фильма c id " + filmId + " успешно удален лайк " +
                    "пользователя с id " + userId + ".");
        } else {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Пользователя c id " + userId + " нет в базе.");
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        Map<Integer, Film> filmsMap = filmStorage.getFilms();
        List<Film> filmsList = new ArrayList<>();
        for (Integer i: filmsMap.keySet()) {
            filmsList.add(filmsMap.get(i));
        }
        return filmsList.stream()
                .sorted()
                .limit(count)
                .collect(Collectors.toList());
    }
}
