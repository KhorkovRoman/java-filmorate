package ru.yandex.practicum.filmorate.storage.film;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Map<Integer, Film> getFilms();

    Collection<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Film film);

}
