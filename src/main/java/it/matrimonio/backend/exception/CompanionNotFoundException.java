package it.matrimonio.backend.exception;

public class CompanionNotFoundException extends RuntimeException {

    public CompanionNotFoundException(Long id) {
        super("Companion with id " + id + " not found");
    }
}