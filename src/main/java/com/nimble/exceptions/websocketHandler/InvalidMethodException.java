package com.nimble.exceptions.websocketHandler;

public class InvalidMethodException extends RuntimeException {

	public InvalidMethodException(String method) {
		super(method + " is not a valid method!!!");
	}

}
