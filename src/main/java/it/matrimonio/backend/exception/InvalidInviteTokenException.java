package it.matrimonio.backend.exception;

public class InvalidInviteTokenException extends RuntimeException {

    public InvalidInviteTokenException() {
        super("Token invito non valido o scaduto");
    }
}