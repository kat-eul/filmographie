package fr.esgi.filmographie.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageException {
    PERSON_NOT_FOUND("No person found with id : %d"),
    MISSING_PERSON_NAME("Person must contain either a nickName, or both firstName and lastName."),
    MOOVIE_NOT_FOUD("No moovie found with id : ");

    private final String message;
}
