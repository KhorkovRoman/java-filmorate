package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class ValidationUser {
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
