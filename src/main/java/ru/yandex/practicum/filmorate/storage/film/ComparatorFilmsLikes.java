package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class ComparatorFilmsLikes implements Comparator<Film> {
    @Override
    public int compare(Film film1, Film film2) {
        return Integer.compare(film2.getLikes().size(), film1.getLikes().size());
    }
}