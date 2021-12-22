package com.nimble.exceptions.Player;

public class InvalidPlayerColorException extends RuntimeException {

	public InvalidPlayerColorException(String playerColor) {
		super(String.format("Fijate que player color (%s) es invalido!!!", playerColor));
	}

}
