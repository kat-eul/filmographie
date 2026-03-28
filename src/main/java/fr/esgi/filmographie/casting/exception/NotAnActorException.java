package fr.esgi.filmographie.casting.exception;

public class NotAnActorException extends RuntimeException {
    public NotAnActorException(String name) {
        super("La personne '" + name + "' n'est pas un acteur et ne peut pas être dans un casting");
    }
}
