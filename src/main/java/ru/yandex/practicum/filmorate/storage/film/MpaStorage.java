package ru.yandex.practicum.filmorate.storage.film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaStorage {
    Mpa getMpaById(Integer mpaId);
    Collection<Mpa> getAllMpa();
}
