package com.github.floriangubler.coworkspacemgr.exception;

public class BookingAlreadyExistsException extends RuntimeException {
    public BookingAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
