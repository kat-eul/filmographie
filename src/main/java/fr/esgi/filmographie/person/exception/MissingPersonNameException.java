package fr.esgi.filmographie.person.exception;

import static fr.esgi.filmographie.enums.MessageException.MISSING_PERSON_NAME;

public class MissingPersonNameException extends RuntimeException{
    public MissingPersonNameException(){
        super(MISSING_PERSON_NAME.getMessage());
    }
}
