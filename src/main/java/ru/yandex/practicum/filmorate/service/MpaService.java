package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.util.Collection;

@Slf4j
@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa getMpaById(Integer mpaId) {
        Mpa mpa = mpaStorage.getMpaById(mpaId);
        if (mpa == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Mpa c id " + mpaId + " нет в базе.");
        }
        log.info("Mpa c id " + mpaId + " найден в базе.");
        return mpa;
    }

    public Collection<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }
}
