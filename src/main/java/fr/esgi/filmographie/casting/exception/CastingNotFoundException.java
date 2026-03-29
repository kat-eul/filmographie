package fr.esgi.filmographie.casting.exception;

import fr.esgi.filmographie.exception.NotFoundException;
import static fr.esgi.filmographie.enums.MessageException.CASTING_NOT_FOUND;

public class CastingNotFoundException extends NotFoundException {
    public CastingNotFoundException(Long movieId, Long roleId, Long personId) {
        super(CASTING_NOT_FOUND.getMessage().formatted(movieId, roleId, personId));
    }
}
