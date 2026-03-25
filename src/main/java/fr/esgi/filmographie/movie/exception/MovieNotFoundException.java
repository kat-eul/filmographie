package fr.esgi.filmographie.movie.exception;

import fr.esgi.filmographie.enums.MessageException;
import fr.esgi.filmographie.exception.NotFoundException;

public class MovieNotFoundException extends NotFoundException {
    public MovieNotFoundException(final Long id) {
        super(MessageException.MOOVIE_NOT_FOUD.getMessage().formatted(id));
    }
}
