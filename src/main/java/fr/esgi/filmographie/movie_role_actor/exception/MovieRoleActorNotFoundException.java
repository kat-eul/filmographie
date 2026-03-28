package fr.esgi.filmographie.movie_role_actor.exception;

import fr.esgi.filmographie.enums.MessageException;
import fr.esgi.filmographie.exception.NotFoundException;

public class MovieRoleActorNotFoundException extends NotFoundException {
    public MovieRoleActorNotFoundException(final Long id) {
        super(MessageException.MOOVIE_NOT_FOUD.getMessage().formatted(id));
    }
}
