package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
public class GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreById(Integer genreId) {
        Genre genre = genreStorage.getGenreById(genreId);
        if (genre == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Жанра c id " + genreId + " нет в базе.");
        }
        log.info("Жанра c id " + genreId + " найден в базе.");
        return genre;
    }

    public Collection<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }
}
