package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
class UserDBStorageTests {

    private User user;
    private final UserStorage userStorage;

    @BeforeEach
    void initFields() {
        user = new User(1, "User@email.com", "UserLogin", "UserName",
                LocalDate.of(2005, 12, 20));

        userStorage.createUser(user);
    }

    @Test
    @Order(1)
    public void testFindUserById() {
        Optional<User> userOptional = Optional.of(userStorage.getUserById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    @Order(2)
    public void testFindAllUsers() {
        Collection<User> users = userStorage.getAllUsers();
        assertEquals(1, users.size());
    }

    @Test
    @Order(3)
    public void testCreateUser() {
        Optional<User> userOptional = Optional.of(userStorage.getUserById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    @Order(4)
    public void testUpdateUser() {
        userStorage.updateUser(user);
        Optional<User> userOptional = Optional.of(userStorage.getUserById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    @Order(5)
    public void testDeleteUser() {
        Collection<User> users = userStorage.getAllUsers();
        assertEquals(1, users.size());

        userStorage.deleteUser(user);
        users = userStorage.getAllUsers();
        assertEquals(0, users.size());
    }
}