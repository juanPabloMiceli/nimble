package com.nimble.exceptions.game;

public class InvalidPlayerNumberException extends Exception {

	public InvalidPlayerNumberException(int n) {
		super("Player number: " + n + " is invalid!!!");
	}
}
