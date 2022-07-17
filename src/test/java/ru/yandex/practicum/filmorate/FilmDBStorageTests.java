package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test.sql")
public class FilmDBStorageTests {

    private Film film;
    private final FilmStorage filmStorage;

    @BeforeEach
    void initFields() {
        film = new Film(1, "FilmName", "FilmDescription",
                LocalDate.of(2005, 12, 20), 90, 4, new Mpa(1, "G"));

        filmStorage.createFilm(film);
    }

    @Test
    @Order(1)
    public void testFindFilmById() {
        Optional<Film> filmOptional = Optional.of(filmStorage.getFilmById(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    @Order(2)
    public void testFindAllFilms() {
        Collection<Film> films = filmStorage.getAllFilms();
        assertEquals(1, films.size());
    }

    @Test
    @Order(3)
    public void testCreateFilm() {
        Optional<Film> filmOptional = Optional.of(filmStorage.getFilmById(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    @Order(4)
    public void testUpdateFilm() {
        filmStorage.updateFilm(film);
        Optional<Film> filmOptional = Optional.of(filmStorage.getFilmById(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    @Order(5)
    public void testDeleteFilm() {
        Collection<Film> films = filmStorage.getAllFilms();
        assertEquals(1, films.size());

        filmStorage.deleteFilm(film);
        films = filmStorage.getAllFilms();
        assertEquals(0, films.size());
    }

}
