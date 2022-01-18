package com.nimble.exceptions.game;

public class InvalidPlayerNumberException extends RuntimeException {

	public InvalidPlayerNumberException(int n) {
		super("Player number: " + n + " is invalid!!!");
	}
}
