package fr.esgi.filmographie.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum JobEnum {
    ACTOR,
    REALISATOR,
    REALISATOR_ACTOR;

    public Set<JobEnum> includedForFilter() {
        return switch (this) {
            case ACTOR -> EnumSet.of(ACTOR, REALISATOR_ACTOR);
            case REALISATOR -> EnumSet.of(REALISATOR, REALISATOR_ACTOR);
            case REALISATOR_ACTOR -> EnumSet.of(REALISATOR_ACTOR);
        };
    }
}
