package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    int userId = 0;

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validateUser(user);
        userId++;
        user.setId(userId);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь " + user + " успешно записан.");
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            validateUser(user);
            users.put(user.getId(), user);
            log.info("Пользователь " + user + " успешно обновлен.");
        } else {
            log.warn("Пользователя с id " + user.getId() + " в базе нет.");
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Пользователя с id " + user.getId() + " в базе нет.");
        }
        return user;
    }

    public void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Электронная почта не может быть пустой и должна содежать символ @." +
                    " Введено {}",user.getEmail());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Электронная почта не может быть пустой и  должна содежать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Логин не может быть пустым и содержать пробелы." +
                    " Введено {}",user.getLogin());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем." +
                    " Введено {}. Дата сегодня {}",user.getBirthday(), LocalDate.now());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Дата рождения не может быть в будущем.");
        }
    }
}