package com.nimble.exceptions.card;

public class InvalidOuterCardColorException extends RuntimeException {

	public InvalidOuterCardColorException(String color) {
		super(String.format("Fijate que outer color (%s) es invalido!!!", color));
	}
}
