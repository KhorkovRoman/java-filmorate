package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film implements Comparable<Film>{
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Integer rate;
    private Set<Integer> likes = new HashSet<>();

    @Override
    public int compareTo(Film o) {
        return Integer.compare(o.likes.size(), this.likes.size());
    }
}
