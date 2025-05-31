package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.TreeSet;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    public Film addLike(long idFilm, long idUser) {
        validation(idFilm, idUser);
        return filmStorage.find(idFilm).addLike(userStorage.find(idUser));
    }

    public Film removeLike(long idFilm, long idUser) {
        validation(idFilm, idUser);
        return filmStorage.find(idFilm).removeLike(userStorage.find(idUser));
    }

    // Метод возвращает фильмов с наибольшим количеством лайков
    public TreeSet<Film> getPopularFilmForLike(long count) {
        if (count < 1) {
            log.warn("Нерпавильный параметр count. Доступный диапазаон 1+. Текущее значение - {}", count);
            throw new ValidationException("Параметр count не может быть меньше 1. Текущий параметр count - " + count);
        }
        TreeSet<Film> films = new TreeSet<>(

                Comparator.comparingInt((Film film) -> film.getLikeUser().size())
                        .thenComparingLong(Film::getId)
                        .reversed()
        );
        log.trace("Создан список фильмов с сортировкой");
        for (Film film : filmStorage.findAll()) {
            films.add(film);
            log.trace("Добавлен новый фильм в список с сортировкой");
            if (films.size() > count) {
                log.trace("Размер списка фильмов с сортировкой превышет допустимый - {}", count);
                films.removeFirst();
                log.trace("Удаление наименьше по сортировке фильм из списка фильмов с сортировкой");
            }
        }
        log.info("Получение фильмов с наибольшим количеством лайков");
        return films;
    }

    private void validation(long idFilm, long idUser) {
        if (idFilm < 0) {
            log.warn("idFilm не может быть ниже нуля. Текущее значение - {}", idFilm);
            throw new ValidationException("idFilm не может быть ниже нуля. Текущее значение - " + idFilm);
        }
        if (idUser < 0) {
            log.warn("idUser не может быть ниже нуля. Текущее значение - {}", idUser);
            throw new ValidationException("idFilm не может быть ниже нуля. Текущее значение - " + idUser);
        }
    }
}
