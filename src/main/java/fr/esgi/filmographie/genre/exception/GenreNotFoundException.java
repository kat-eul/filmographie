package fr.esgi.filmographie.genre.exception;

import fr.esgi.filmographie.exception.NotFoundException;

import static fr.esgi.filmographie.enums.MessageException.GENRE_NOT_FOUND;

public class GenreNotFoundException extends NotFoundException {
    public GenreNotFoundException(Long id) {super(GENRE_NOT_FOUND.getMessage().formatted(id));}
}
