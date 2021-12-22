package com.nimble.exceptions.card;

public class InvalidBackCardColorException extends RuntimeException {

	public InvalidBackCardColorException(String backColor) {
		super(String.format("Fijate que back color (%s) es invalido!!!", backColor));
	}

}
