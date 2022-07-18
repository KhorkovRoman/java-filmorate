package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Integer rate;
    @NotNull
    private Mpa mpa;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();

    public Film(Integer id, String name, String description,
                LocalDate release_date, int duration, int rate, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = release_date;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
    }
}
