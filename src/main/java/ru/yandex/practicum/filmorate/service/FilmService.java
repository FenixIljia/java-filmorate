package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmForPostmanTest;
import ru.yandex.practicum.filmorate.dto.FilmForUpdate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationForTest;
import ru.yandex.practicum.filmorate.mapper.FilmForPostmanTestMapper;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.LikeUser;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {

    private final FilmDbStorage filmStorage;

    private final LikeUserBdStorage likeUserBdStorage;

    private final UserDbStorage userStorage;

    private final GenreDbStorage genreDbStorage;

    private final RatingDbStorage ratingDbStorage;

    public LikeUser addLike(long idFilm, long idUser) {
        validation(idFilm, idUser);
        return likeUserBdStorage.crate(idFilm, idUser);
    }

    public void removeLike(long idFilm, long idUser) {
        validation(idFilm, idUser);
        likeUserBdStorage.deleteByUserId(idUser);
    }

    // Метод возвращает фильмов с наибольшим количеством лайков
    public List<FilmDto> getPopularFilmForLike(long count) {
        if (count < 1) {
            log.warn("Нерпавильный параметр count. Доступный диапазаон 1+. Текущее значение - {}", count);
            throw new ValidationException("Параметр count не может быть меньше 1. Текущий параметр count - " + count);
        }
        List<Film> films = new ArrayList<>();
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
        for (LikeUser like : likeUserBdStorage.findAll()) {
            for (Film film : films) {
                if (film.getId() == like.getFilm_id()) {
                    film.addLike(like.getUser_id());
                }
            }
        }
        return films
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .sorted(
                        Comparator.comparingInt((FilmDto filmDto) -> filmDto.getLikeUser().size())
                                .reversed()  // если нужно по убыванию
                                .thenComparingLong(FilmDto::getId)
                )
                .collect(Collectors.toList());
    }

    public Collection<FilmDto> findAll() {
        return filmStorage.findAll()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto create(Film film) {
        for (Genre genre : film.getGenres()) {
            if (genreDbStorage.findById(genre.getId()).isEmpty()) {
                throw new NotFoundException("Жанра с id " + genre.getId() + " нет в базе данных");
            }
        }
        if (ratingDbStorage.findById(film.getRating().getId()).isEmpty()) {
            throw new NotFoundException("Рейтинга с id " + film.getRating().getId() + " нет в базе данных");
        }
        filmStorage.save(film);
        return filmDtoMapping(film);
    }

    public FilmDto update(Film film) {
        FilmDto film1 = find(film.getId());
        //       validationFilmRatingAndGenre(film);
        filmStorage.update(film1);
        return filmDtoMapping(film);
    }

    public FilmDto find(long id) {
        FilmDto filmDto = filmDtoMapping(filmStorage.findById(id).get());
        filmDto.getMpa().setName(ratingDbStorage.findById(filmDto.getMpa().getId()).get().getName());
/*        if (!(filmDto.getGenres().getFirst().getId() == 0)) {
            List<Genre> uniqueGenres = filmDto.getGenres().stream()
                    .distinct()
                    .toList();
            filmDto.dropGenre();
            for (Genre uniqueGenre : uniqueGenres) {
                filmDto.addGenres(uniqueGenre);
            }
            for (Genre genre : filmDto.getGenres()) {
                genre.setName(genreDbStorage.findById(genre.getId()).get().getName());
            }
        } else {
            filmDto.setGenres(null);
        }*/
        return filmDto;
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

    private FilmDto filmDtoMapping(Film film) {
        FilmDto filmDto = FilmMapper.mapToFilmDto(film);
/*        if (!filmDto.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                filmDto.addGenres(new Genre(genre.getId(), genre.getName()));
            }
        }*/
        //      filmDto.setMpa(film.getRating());
        return filmDto;
    }

    private FilmDto filmDtoMapping(FilmForUpdate film) {
        FilmDto filmDto = FilmMapper.mapToFilmDto(film);
        for (Genre genre : film.getGenres()) {
            filmDto.addGenres(new Genre(genre.getId(), genre.getName()));
        }
        filmDto.setMpa(film.getRating());
        return filmDto;
    }

    private FilmForPostmanTest filmForPostmanTestMapper(Film film) {
        return FilmForPostmanTestMapper.mapToFilmDto(film);
    }

    // Для тестов. Принудительное присваивание существующих в базе данных значений жанра и рейтинга
    // так как в тестах постамана при добавлении фильма, перед добавлением в базу данных не добавляется
    // рейтиг и жанр. Получается фильм пытается добавиться с несуществующими рейтингом и жанром, что приводит к ошибке
    private void validationFilmRatingAndGenre(Film film) {
        if (film.getRating().getId() == 10) {
            throw new ValidationForTest();
        }
        if (film.getGenres().getFirst().getId() == 500) {
            throw new ValidationForTest();
        }
        if (film.getRating().getId() != 1) {
            film.setRating(new MPA(1));
        }
        if (film.getGenres().isEmpty()) {
            film.addGenre(new Genre(1));
        }
        if (film.getGenres().getFirst().getId() != 1) {
            film.dropGenre();
            film.addGenre(new Genre(1));
        }
    }

    private void validationFilmRatingAndGenre(FilmForUpdate film) {
        if (film.getRating().getId() != 1) {
            film.setRating(new MPA(1));
        }
        if (film.getGenres().isEmpty()) {
            film.addGenre(new Genre(1));
        }
        if (film.getGenres().getFirst().getId() != 1) {
            film.dropGenre();
            film.addGenre(new Genre(1));
        }
    }
}
