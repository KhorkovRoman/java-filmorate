package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmControllerTest {


	Film film = new Film();

	FilmController filmController = new FilmController();

    @Test
	void filmCreateValidationName() {
		film.setName("");
		assertThrows(ValidationException.class, () -> filmController.create(film));

		film.setName("Name");
		film.setDescription("Description");
		film.setReleaseDate(LocalDate.of(1896, 12, 28));
		film.setDuration(90);

		filmController.create(film);
		assertTrue(filmController.findAll().contains(film));
	}

	@Test
	void filmCreateValidationDescription() {
		film.setDescription("fgj;s,ewlrpg;bmlgng;lnmjdklgjalksjflksdfjaldskfj;alsdfj;alks" +
				"djf;alsdjf;alksdjf;alksdjflaksdjfkdjfakdljfkldasjffgj;s,ewlrpg;bmlgng;lnm" +
				"jdklgjalksjflksdfjaldskfj;alsdfj;alksdjf;alsdjf;alksdjf;alksdjflaksdjfkdjfakdljfkldasjf");
		assertThrows(ValidationException.class, () -> filmController.create(film));

		film.setName("Name");
		film.setDescription("Description");
		film.setReleaseDate(LocalDate.of(1896, 12, 28));
		film.setDuration(90);

		filmController.create(film);
		assertTrue(filmController.findAll().contains(film));
	}

	@Test
	void filmCreateValidationReleaseDate() {
		film.setReleaseDate(LocalDate.of(1896, 12, 28));
		assertThrows(ValidationException.class, () -> filmController.create(film));

		film.setName("Name");
		film.setDescription("Description");
		film.setReleaseDate(LocalDate.of(1896, 12, 28));
		film.setDuration(90);

		filmController.create(film);
		assertTrue(filmController.findAll().contains(film));
	}

	@Test
	void filmCreateValidationDuration() {
		film.setDuration(-25);
		assertThrows(ValidationException.class, () -> filmController.create(film));

		film.setName("Name");
		film.setDescription("Description");
		film.setReleaseDate(LocalDate.of(1896, 12, 28));
		film.setDuration(90);

		filmController.create(film);
		assertTrue(filmController.findAll().contains(film));
	}
}
