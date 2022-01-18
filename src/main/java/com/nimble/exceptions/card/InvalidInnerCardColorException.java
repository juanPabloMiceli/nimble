package com.nimble.exceptions.card;

public class InvalidInnerCardColorException extends RuntimeException {

	public InvalidInnerCardColorException(String color) {
		super(String.format("Fijate que inner color (%s) es invalido!!!", color));
	}
}
