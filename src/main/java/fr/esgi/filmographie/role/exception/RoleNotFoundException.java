package fr.esgi.filmographie.role.exception;

import fr.esgi.filmographie.exception.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException(final Long id) {
        super("No role found with id : %d".formatted(id));
    }
}
