package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test.sql")
public class GenreDBStorageTests {

    private Film film;
    private final FilmStorage filmStorage;

    private LinkedHashSet<Genre> genres;
    private final GenreStorage genreStorage;

    @BeforeEach
    void initFields() {

        genres = new LinkedHashSet<>();
        genres.add(new Genre(1, "Комедия"));

        film = new Film(1, "FilmName", "FilmDescription",
                LocalDate.of(2005, 12, 20), 90, 4,
                new Mpa(1, "G"), genres);

        filmStorage.createFilm(film);
    }

    @Test
    @Order(1)
    public void testGetGenreById() {
        Optional<Genre> genreOptional = Optional.of(genreStorage.getGenreById(1));
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    @Order(2)
    public void testGetAllGenres() {
        Collection<Genre> genres = genreStorage.getAllGenres();
        assertEquals(6, genres.size());
        assertEquals("Комедия", genres.stream().findFirst().get().getName());
    }

    @Test
    @Order(3)
    public void testSetFilmGenres() {
        genreStorage.setFilmGenres(film);
        Collection<Genre> genres = genreStorage.loadFilmGenres(film.getId());
        System.out.println(genres);
        assertEquals("Комедия", genres.stream().findFirst().get().getName());
    }

    @Test
    @Order(4)
    public void testLoadFilmGenres() {
        genreStorage.setFilmGenres(film);
        Collection<Genre> genres = genreStorage.loadFilmGenres(film.getId());
        assertEquals(1, genres.size());
    }
}
