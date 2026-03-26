package fr.esgi.filmographie.role.exception;

import fr.esgi.filmographie.exception.NotFoundException;

import static fr.esgi.filmographie.enums.MessageException.ROLE_NOT_FOUND;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException(final Long id) {
        super(ROLE_NOT_FOUND.getMessage().formatted(id));
    }
}
