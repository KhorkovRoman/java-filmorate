package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping
    private void delete(@Valid @RequestBody Film film) {
        filmService.deleteFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }
}