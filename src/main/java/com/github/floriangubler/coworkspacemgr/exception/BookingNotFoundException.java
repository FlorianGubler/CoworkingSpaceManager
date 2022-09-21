package com.github.floriangubler.coworkspacemgr.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
