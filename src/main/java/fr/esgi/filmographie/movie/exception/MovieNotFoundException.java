package fr.esgi.filmographie.movie.exception;

import fr.esgi.filmographie.exception.NotFoundException;

import static fr.esgi.filmographie.enums.MessageException.MOVIE_NOT_FOUD;

public class MovieNotFoundException extends NotFoundException {
    public MovieNotFoundException(final Long id) {
        super(MOVIE_NOT_FOUD.getMessage().formatted(id));
    }
}
