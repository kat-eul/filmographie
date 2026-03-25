package fr.esgi.filmographie.movie.exception;

import fr.esgi.filmographie.exception.NotFoundException;

public class MovieNotFoundException extends NotFoundException {
    public MovieNotFoundException(final Long id) {
        super("No movie found with id : %d".formatted(id));
    }
}
