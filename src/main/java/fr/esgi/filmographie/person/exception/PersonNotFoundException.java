package fr.esgi.filmographie.person.exception;

import fr.esgi.filmographie.exception.NotFoundException;
import static fr.esgi.filmographie.enums.MessageException.PERSON_NOT_FOUND;

public class PersonNotFoundException extends NotFoundException {
    public PersonNotFoundException(Long id){
        super(PERSON_NOT_FOUND.getMessage().formatted(id));
    }
}
