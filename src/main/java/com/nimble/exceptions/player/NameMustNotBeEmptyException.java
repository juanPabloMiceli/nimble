package com.nimble.exceptions.player;

public class NameMustNotBeEmptyException extends RuntimeException {

	public NameMustNotBeEmptyException() {
		super("Fijate que el nombre esta vacio amigazo!!!");
	}
}
