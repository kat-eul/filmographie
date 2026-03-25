package fr.esgi.filmographie.exception;

import fr.esgi.filmographie.enums.MessageException;

public class MissingPersonNameException extends RuntimeException{
    public MissingPersonNameException(){
        super(MessageException.MISSING_PERSON_NAME.getMessage());
    }
}
