package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.ValidationUser;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private final ValidationUser validationUser;

    @Autowired
    public UserService(UserStorage userStorage, ValidationUser validationUser) {
        this.userStorage = userStorage;
        this.validationUser = validationUser;
    }

    public User createUser(User user) {
        validationUser.validateUser(user);
        return userStorage.createUser(user);
    }

    public User getUserById(Integer userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Пользователя c id " + userId + " нет в базе.");
        }
        log.info("Пользователь c id " + userId + " найден в базе.");
        return user;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User updateUser(User user) {
        validateUser(user.getId());
        validationUser.validateUser(user);
        return userStorage.updateUser(user);
    }

    public void deleteUser(User user) {
        validationUser.validateUser(user);
        userStorage.deleteUser(user);
    }

    public void validateUser(Integer userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Пользователя c id " + userId + " нет в базе.");
        }
    }
}
