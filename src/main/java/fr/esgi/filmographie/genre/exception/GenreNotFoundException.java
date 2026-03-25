package fr.esgi.filmographie.genre.exception;

import fr.esgi.filmographie.exception.NotFoundException;

public class GenreNotFoundException extends NotFoundException {
    public GenreNotFoundException(Long id) {
        super("No genre found with id : %d".formatted(id));
    }
}
