package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {
    Film addLike(Integer filmId, Integer userId);
    void deleteLike(Integer filmId, Integer userId);
    List<Film> getPopularFilms(Integer count);
}
