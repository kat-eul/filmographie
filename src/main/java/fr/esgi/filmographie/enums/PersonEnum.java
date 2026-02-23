package fr.esgi.filmographie.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PersonEnum {
    ACTOR("Actor"),
    REALISATOR("Realisator"),
    REALISATOR_ACTOR("Realisator and Actor");

    private final String jobName;
}
