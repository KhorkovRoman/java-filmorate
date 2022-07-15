package ru.yandex.practicum.filmorate.storage.film;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.LinkedHashSet;

public interface GenreStorage {
    Genre getGenreById(Integer genreId);
    Collection<Genre> getAllGenres();
    void setFilmGenres(Film film);
    LinkedHashSet<Genre> loadFilmGenres(Integer filmId);

}
