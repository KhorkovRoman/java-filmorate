package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test.sql")
public class LikeDBStorageTests {

    private User user;
    private Film film;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;


    @BeforeEach
    void initFields() {
        user = new User(1, "User@email.com", "UserLogin", "UserName",
                LocalDate.of(2005, 12, 20));
        userStorage.createUser(user);
;
        film = new Film(1, "FilmName", "FilmDescription",
                LocalDate.of(2005, 12, 20), 90, 4, new Mpa(1, "G"));
        filmStorage.createFilm(film);

        likeStorage.addLike(user.getId(), film.getId());
    }

    @Test
    @Order(6)
    public void testAddLike() {
        List<Film> films = likeStorage.getPopularFilms(10);
        assertEquals(1, films.size());
    }

    @Test
    @Order(7)
    public void testDeleteLike() {
        List<Film> films = likeStorage.getPopularFilms(10);
        assertEquals(1, films.size());

        likeStorage.deleteLike(user.getId(), film.getId());
        films = likeStorage.getPopularFilms(10);
        assertEquals(1, films.size());
    }

    @Test
    @Order(8)
    public void testGetPopularFilms() {
        List<Film> films = likeStorage.getPopularFilms(10);
        assertEquals(1, films.size());
    }
}
