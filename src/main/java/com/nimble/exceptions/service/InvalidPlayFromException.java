package com.nimble.exceptions.service;

public class InvalidPlayFromException extends RuntimeException {

	public InvalidPlayFromException(String playFrom) {
		super("You can't play from: " + playFrom);
	}
}
