package it.matrimonio.backend.exception;

public class GuestNotFoundException extends RuntimeException {

    public GuestNotFoundException(Long id) {
        super("Guest non trovato con id: " + id);
    }
}