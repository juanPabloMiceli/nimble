package com.nimble.exceptions;

public class KickException extends Exception{
    public KickException(int kicked, String description) {
        super(String.format("Unable to kick %d: %s", kicked, description));
    }
}
