package fr.esgi.filmographie.person.exception;

import fr.esgi.filmographie.enums.MessageException;
import fr.esgi.filmographie.exception.NotFoundException;

public class PersonNotFoundException extends NotFoundException {
    public PersonNotFoundException(Long id){
        super(MessageException.PERSON_NOT_FOUND.getMessage().formatted(id));
    }
}
