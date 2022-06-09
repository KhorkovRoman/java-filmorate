package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

	private final Film film = new Film();

	User user = new User();

	UserController userController = new UserController();

    @Test
	void userCreateValidationEmail() {
		user.setEmail("");
		assertThrows(ValidationException.class, () -> userController.create(user));
		user.setEmail("mail.ru");
		assertThrows(ValidationException.class, () -> userController.create(user));

		user.setEmail("mail@mail.ru");
		user.setLogin("login");
		user.setName("name");
		user.setBirthday(LocalDate.of(2005, 12, 20));

		userController.create(user);
		assertTrue(userController.findAll().contains(user));
	}

	@Test
	void userCreateValidationLogin() {
		user.setLogin("");
		assertThrows(ValidationException.class, () -> userController.create(user));
		user.setLogin("login user");
		assertThrows(ValidationException.class, () -> userController.create(user));
		user.setLogin("");
		assertThrows(ValidationException.class, () -> userController.create(user));

		user.setEmail("mail@mail.ru");
		user.setLogin("login");
		user.setName("name");
		user.setBirthday(LocalDate.of(2005, 12, 20));

		userController.create(user);
		assertTrue(userController.findAll().contains(user));
	}

	@Test
	void userCreateValidationBirthday() {
		user.setBirthday(LocalDate.of(2045, 12, 25));
		assertThrows(ValidationException.class, () -> userController.create(user));

		user.setEmail("mail@mail.ru");
		user.setLogin("login");
		user.setName("name");
		user.setBirthday(LocalDate.of(2005, 12, 20));

		userController.create(user);
		assertTrue(userController.findAll().contains(user));
	}
}
