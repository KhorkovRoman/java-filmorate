package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    ValidationUser validationUser;
    private int userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Autowired
    public InMemoryUserStorage(ValidationUser validationUser) {
        this.validationUser = validationUser;
    }

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    public User getUserById(Integer userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        validationUser.validateUser(user);
        userId++;
        user.setId(userId);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь " + user + " успешно записан.");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            validationUser.validateUser(user);
            users.put(user.getId(), user);
            log.info("Пользователь " + user + " успешно обновлен.");
        } else {
            log.warn("Пользователя с id " + user.getId() + " в базе нет.");
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Пользователя с id " + user.getId() + " в базе нет.");
        }
        return user;
    }

    @Override
    public void deleteUser(User user) {
        if (users.containsKey(user.getId())) {
            validationUser.validateUser(user);
            users.remove(user.getId());
            log.info("Пользователь " + user + " успешно удален.");
        } else {
            log.warn("Пользователя с id " + user.getId() + " в базе нет.");
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Пользователя с id " + user.getId() + " в базе нет.");
        }
    }
}
