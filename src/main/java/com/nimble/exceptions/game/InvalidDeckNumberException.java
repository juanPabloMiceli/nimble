package com.nimble.exceptions.game;

public class InvalidDeckNumberException extends RuntimeException {

	public InvalidDeckNumberException(int n) {
		super("Deck number: " + n + " is invalid!!!");
	}

}
