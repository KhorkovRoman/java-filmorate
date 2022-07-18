package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class LikeService {

    private final LikeStorage likeStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public LikeService(LikeStorage likeStorage, FilmStorage filmStorage, UserStorage userStorage) {
        this.likeStorage = likeStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(Integer filmId, Integer userId) {
        validateFilm(filmId);
        validateUser(userId);
        return likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        validateFilm(filmId);
        validateUser(userId);
        likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return likeStorage.getPopularFilms(count);
    }

    public void validateUser(Integer userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Пользователя c id " + userId + " нет в базе.");
        }
    }

    public void validateFilm(Integer filmId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Фильма c id " + filmId + " нет в базе.");
        }
    }
}
