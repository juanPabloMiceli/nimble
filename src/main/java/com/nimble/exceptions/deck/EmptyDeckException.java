package com.nimble.exceptions.deck;

public class EmptyDeckException extends RuntimeException {

	public EmptyDeckException() {
		super("Fijate que estas queriendo usar cartas de un mazo vacio!!!");
	}
}
