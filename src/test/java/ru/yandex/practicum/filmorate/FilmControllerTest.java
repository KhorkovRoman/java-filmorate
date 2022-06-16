package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
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

	private Film film;
	private FilmController filmController;
	private String DESCRIPTION_LENGTH_OVER_200 = "fgj;s,ewlrpg;bmlgng;lnmjdklgja" +
			"lksjflksdfjaldskfj;alsdfj;alksdjf;alsdjf;alksdjf;alksdjflaksdjfkdjfakdljfkldasjf" +
			"fgj;s,ewlrpg;bmlgng;lnmjdklgjalksjflksdfjaldskfj;alsdfj;alksdjf;alsdjf;alksdjf;a" +
			"lksdjflaksdjfkdjfakdljfkldasjf";

	@BeforeEach
	void initFields() {
//		film = new Film();
//		filmController = new FilmController(inMemoryFilmStorage);
	}

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
		film.setDescription(DESCRIPTION_LENGTH_OVER_200);
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
